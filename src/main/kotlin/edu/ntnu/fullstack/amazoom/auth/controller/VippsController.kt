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
        response: HttpServletResponse,
        request: HttpServletRequest
    ) {
        // Handle if the callback is not successful
        params.get("error")?.let {
            logger.error("Error during vipps authentication: $it | ${params["error_description"]}");
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Failed to authenticate with vipps",
                "Vipps sent an error: ${params["error_description"]}"
            )
            return response.sendRedirect("/register");
        };

        val code = params.getOrElse("code") {
            logger.error("Missing code parameter in vipps authentication");
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Failed to authenticate with vipps",
            )
            return response.sendRedirect("/register");
        };

        val vippsToken = vippsService.getToken(code, vippsService.createVippsCallbackUrl(request)).getOrElse {
            logger.error("Error during vipps token retrieval: ${it.message}");
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Failed to authenticate with vipps",
            )
            return response.sendRedirect("/register");
        }.access_token;

        val userResult = vippsService.getUserInfo(vippsToken);

        if (userResult.isFailure) {
            logger.error("Error during vipps user info retrieval: ${userResult.exceptionOrNull().toString()}");
            Utils.addToastToResponse(
                response,
                ToastType.Error,
                "Vipps authentication failed",
                "Failed to get user info from the Vipps API"
            )
            return response.sendRedirect("/register");
        }
        val vippsUser = userResult.getOrNull()!!;

        var user = runCatching {
            userService.getUserByNin(vippsUser.nin)
        }.getOrElse {
            // If the user is not found, we create a new user in the database
            logger.info("Vipps user not found in database, creating new user for ${vippsUser.email}");
            runCatching {
                userService.createUser(CreateUserDto(
                    name = vippsUser.name,
                    email = vippsUser.email,
                    phoneNumber = vippsUser.phone_number.substring(2),
                    nin = vippsUser.nin
                ))
            }.getOrElse {
                logger.error("Error during vipps user creation: ${it.message}");
                Utils.addToastToResponse(
                    response,
                    ToastType.Error,
                    "Failed to register using vipps",
                    it.message
                )
                return response.sendRedirect("/register");
            }
        };

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
        logger.info("Starting vipps authentication flow");
        val callbackUrl = vippsService.createVippsCallbackUrl(request);
        logger.info("Callback URL created: $callbackUrl");

        val authEndpoint = vippsService.createVippsAuthUrl(callbackUrl);
        logger.debug("Redirecting user to vipps: $authEndpoint");

        response.sendRedirect(authEndpoint);
    }
}