package app.kingmojang.global.security.oauth2

import app.kingmojang.global.property.OAuth2Property
import app.kingmojang.global.util.CookieUtils
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.io.IOException
import java.net.URI

@Component
class OAuth2AuthenticationSuccessHandler(
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val oAuth2Property: OAuth2Property,
) : SimpleUrlAuthenticationSuccessHandler() {

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {
        val targetUrl = determineTargetUrl(request, response, authentication)
        if (response.isCommitted) {
            logger.debug("Response has already been committed. Unable to redirect to $targetUrl")
            return
        }
        clearAuthenticationAttributes(request, response)
        redirectStrategy.sendRedirect(request, response, targetUrl)
    }

    override fun determineTargetUrl(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ): String {
        val targetUrl: String = CookieUtils.getCookie(
            request,
            HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME
        )?.let { it.value } ?: defaultTargetUrl

        if (!isAuthorizedRedirectUri(targetUrl)) {
            throw RuntimeException(
                "Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication"
            )
        }

        val oAuth2UserImpl = authentication.principal as OAuth2UserImpl
        val oAuth2UserInfo = oAuth2UserImpl.oAuth2UserInfo

        if (isFirstLoginWithoutSignup(oAuth2UserImpl, targetUrl)) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "You don't have any registered information, please signup and login.")
                .build().toUriString()
        }
        return UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("email", oAuth2UserInfo.getEmail())
            .queryParam("provider", (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId)
            .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isFirstLoginWithoutSignup(oAuth2UserImpl: OAuth2UserImpl, targetUrl: String) =
        oAuth2UserImpl.authorities.any { it.authority == "ROLE_GUEST" } && targetUrl.contains("/login")

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)

        return oAuth2Property.authorizedRedirectUris.any {
            val authorizedURI = URI.create(it)
            (authorizedURI.host.equals(
                clientRedirectUri.host,
                ignoreCase = true
            ) && authorizedURI.port == clientRedirectUri.port)
        }

    }
}