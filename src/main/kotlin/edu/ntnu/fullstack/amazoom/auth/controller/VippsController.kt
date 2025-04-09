package edu.ntnu.fullstack.amazoom.auth.controller

import edu.ntnu.fullstack.amazoom.ToastType
import edu.ntnu.fullstack.amazoom.Utils
import edu.ntnu.fullstack.amazoom.auth.service.AuthService
import edu.ntnu.fullstack.amazoom.auth.service.VippsService
import edu.ntnu.fullstack.amazoom.common.dto.CreateUserDto
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

/**
 * REST controller for authentication operations.
 * Handles user registration, login, and profile information.
 */
@RestController
@RequestMapping("/api/auth/vipps")
class VippsController(
    private val authService: AuthService,
    private val userService: UserService,
    private val vippsService: VippsService,
) {
    private val logger = LoggerFactory.getLogger(VippsController::class.java)

    /**
     * Registers a new user in the system.
     *
     * @param request The registration request data
     * @return Authentication response with access token
     */
    @GetMapping("/callback")
    fun register(
        @RequestParam params: Map<String, String>,
        response: HttpServletResponse
    ) {
        val code = params.get("code");
        val error = params.get("error");

        if (error != null) {
            logger.error("Error during vipps authentication: $error");
            val errorDescription =
                params.getOrDefault("description", "Faild to authenticate with vipps");

            Utils.addToastToResponse(response, ToastType.Error, errorDescription)
            return response.sendRedirect("/register")
        }

        if (code == null) {
            // This should not in theory happen, but if it does, we should log it and redirect the user to the register page
            logger.error("Missing code parameter in vipps authentication");
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Missing code parameter in vipps authentication"
            );
            return response.sendRedirect("/register")
        }

        val tokenResult = vippsService.getToken(code);
        val tokenData = tokenResult.getOrNull()
        if (tokenData == null) {
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Vipps authentication failed: ${tokenResult.exceptionOrNull()?.message}"
            )
            return response.sendRedirect("/register");
        }

        val userResult = vippsService.getUserInfo(tokenData.access_token);

        val userData = userResult.getOrNull();
        if (userData == null) {
            logger.error("Error during vipps authentication: ${userResult.exceptionOrNull().toString()}");
            Utils.addToastToResponse(response, ToastType.Error, "Failed to get user info from vipps");
            return response.sendRedirect("/register");
        }

        val dbUserResult = kotlin.runCatching { userService.getUserByNin(userData.nin) };
        val user = dbUserResult.fold(
            onFailure = {
                kotlin.runCatching {
                    userService.createUser(
                        CreateUserDto(
                            firstName = userData.given_name,
                            lastName = userData.family_name,
                            email = userData.email,
                            phoneNumber = userData.phone_number.substring(
                                2,
                                userData.phone_number.length
                            ),
                            nin = userData.nin,
                        )
                    )
                }.fold(
                    onFailure = {
                        logger.error("Failed to create vipps user: ${it.message}");
                        Utils.addToastToResponse(response, ToastType.Error, "Failed to register using vipps");
                        return response.sendRedirect("/register");
                    },
                    onSuccess = { it }
                )
            },
            onSuccess = { it }
        );

        val accessToken = authService.generateAccessTokenForUser(user.email);
        val authCookie = authService.createAuthCookie(accessToken);

        logger.debug("Setting auth cookie: am_session=${accessToken}");
        response.addCookie(authCookie);
        return response.sendRedirect("/");
    }

    @GetMapping("/login")
    fun initiate(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {

        val requestUrl = request.requestURL.toString();
        logger.info("Starting Vipps authentication: $requestUrl");

        val url = URI(requestUrl).toURL();
        logger.info("URL authentication: ${url.host} | ${url.path} | ${url.port} | ${url.protocol}",);

        val sb = StringBuilder("${url.protocol}://${url.host}");

        if (url.port != -1) {
            sb.append(":${url.port}");
        }
        sb.append("/api/auth/vipps/callback");

        val callbackUrl = sb.toString();
        logger.debug("Setting callback URL to: $callbackUrl");
        val authEndpoint = vippsService.createVippsAuthUrl(callbackUrl);
        logger.debug("Redirecting user to vipps authentication: $authEndpoint");

        response.sendRedirect(authEndpoint);
    }
}