package edu.ntnu.fullstack.amazoom.auth.service

import edu.ntnu.fullstack.amazoom.Utils
import edu.ntnu.fullstack.amazoom.auth.config.VippsProperties
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Service
class VippsService(
    private val vippsProperties: VippsProperties,
) {
    private val vippsApi = WebClient.builder()
        .baseUrl(vippsProperties.baseUrl)
        .defaultHeaders { headers ->
            headers.accept = listOf(MediaType.APPLICATION_JSON);
            headers.set("Vipps-System-Name", "Amazoom")
            headers.set("Vipps-System-Description", "Amazoom")
            headers.set("Merchant-Serial-Number", vippsProperties.merchantSerialNumber)
            headers.set("Ocp-Apim-Subscription-Key", vippsProperties.ocpApimSubkey)
        }
        .build()

    @OptIn(ExperimentalEncodingApi::class)
    private val client_credentials =
        Base64.Default.encode("${vippsProperties.clientId}:${vippsProperties.clientSecret}".encodeToByteArray());
    private val logger = LoggerFactory.getLogger(VippsService::class.java)


    /**
     * Gets the token from Vipps given the code from auth flow and a redirect URL.
     *
     * @param code The code from the auth flow
     * @param redirectUrl The redirect URL for vipps that was also used in the auth flow
     * @return A result of the token response
     */
    fun getToken(code: String, redirectUrl: String): Result<VippsTokenResponse> {
        val requestBody = Utils.SearchParams(
            mapOf(
                "grant_type" to "authorization_code",
                "code" to code,
                "redirect_uri" to redirectUrl,

                // These are optional, but we include them for maximum support
                "client_id" to vippsProperties.clientId,
                "client_secret" to vippsProperties.clientSecret,
            )
        );
        logger.debug("Requesting token from Vipps with code: $code");
        logger.debug("Request body: $requestBody");
        return kotlin.runCatching {
            vippsApi.post()
                .uri("/access-management-1.0/access/oauth2/token")
                .headers { h ->
                    h.set("Authorization", "Basic $client_credentials")
                    h.set("Content-Type", "application/x-www-form-urlencoded")
                }
                .bodyValue(requestBody)
                .exchangeToMono { response ->
                    if (response.statusCode().isError) {
                        response.bodyToMono(VippsTokenErrorResponse::class.java)
                            .handle<VippsTokenResponse> { errorResponse, sink -> sink.error(
                                VippsTokenException(errorResponse)
                            ) }
                    } else {
                        response.bodyToMono(VippsTokenResponse::class.java)
                    }
                }
                .block() ?: throw RuntimeException("Empty response from Vipps API")
        }
    }

    /**
     * Gets the user info from Vipps API.
     *
     * @param accessToken The access token from the auth flow
     * @return A resilt of the user info
     */
    fun getUserInfo(accessToken: String): Result<VippsUserInfoResponse> {
        return kotlin.runCatching {
            vippsApi.get()
                .uri("/vipps-userinfo-api/userinfo/")
                .headers { h ->
                    h.setBearerAuth(accessToken);
                    h.set("Content-Type", "application/json");
                }
                .exchangeToMono { response ->
                    if (response.statusCode().isError) {
                        response.bodyToMono(VippsUserErrorResponse::class.java)
                            .handle<VippsUserInfoResponse> { errorResponse, sink -> sink.error(
                                VippsUserInfoException(errorResponse)
                            ) }
                    } else {
                        response.bodyToMono(VippsUserInfoResponse::class.java)
                    }
                }
                .block() ?: throw RuntimeException("Empty response from Vipps API")
        }
    }

    /**
     * Creates the URL for the Vipps authentication redirect.
     *
     * @param redirectUrl The URL Vipps should redirect to after authentication.
     * @return The URL for to redirect to.
     */
    fun createVippsAuthUrl(redirectUrl: String): String {
        val params = Utils.SearchParams(mapOf(
            "client_id" to vippsProperties.clientId,
            "response_type" to "code",
            "redirect_uri" to redirectUrl,
            "scope" to "openid nin name email address phoneNumber",
            "state" to "12345"
        ));
        return "${vippsProperties.baseUrl}/access-management-1.0/access/oauth2/auth?${params}"
    }

    /**
     * Creates the URL for the Vipps authentication redirect based on the request host.
     *
     * @param request The current request
     * @return The URL for to redirect to.
     */
    fun createVippsCallbackUrl(request: HttpServletRequest): String {
        val requestUrl = request.requestURL.toString();
        val url = URI(requestUrl).toURL();
        val sb = StringBuilder("${url.protocol}://${url.host}");
        if (url.port != -1) {
            sb.append(":${url.port}");
        }
        sb.append("/api/auth/vipps/callback");
        return sb.toString();
    }
}

data class VippsUserInfoResponse(
    val address: UserAddress,
    val other_addresses: List<UserAddress>,
    val email: String,
    val name: String,
    val family_name: String,
    val given_name: String,
    val phone_number: String,
    val nin: String,
)

data class UserAddress(
    val address_type: String,
    val country: String,
    val formatted: String,
    val postal_code: String,
    val region: String,
    val street_address: String,
)

data class VippsUserErrorResponse(
    val title: String,
    val status: Int,
    val detail: String,
)
class VippsUserInfoException(
    val data: VippsUserErrorResponse,
): RuntimeException("Vipps user info error: ${data.title} (${data.status}): ${data.detail}")


data class VippsTokenResponse(
    val access_token: String,
    val expires_in: Int,
    val id_token: String,
    val token_type: String,
    val scope: String,
)

data class VippsTokenErrorResponse(
    val error: String,
    val error_code: Int,
    val error_debug: String?
);

class VippsTokenException(
    val data: VippsTokenErrorResponse,
): RuntimeException("Vipps token error: ${data.error} (${data.error_code}): ${data.error_debug}")
