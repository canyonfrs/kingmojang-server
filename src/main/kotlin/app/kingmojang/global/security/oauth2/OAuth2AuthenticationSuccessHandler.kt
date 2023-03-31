package app.kingmojang.global.security.oauth2

import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import app.kingmojang.global.property.OAuth2Property
import app.kingmojang.global.security.JwtUtils
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
    private val jwtUtils: JwtUtils,
    private val oAuth2Property: OAuth2Property,
) : SimpleUrlAuthenticationSuccessHandler() {

    companion object {
        const val INVALID_LOGIN_REQUEST = "You don't have any registered information, please signup and login."
        const val INVALID_SIGNUP_REQUEST = "You are a registered member, please login."
        const val BAD_REQUEST = "Bad Request"
    }

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
            throw CommonException(ErrorCodes.INVALID_REDIRECT_URI)
        }

        return if (authentication.principal is OAuth2UserImpl && isTargetUrlContainsLogin(targetUrl)) {
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", INVALID_LOGIN_REQUEST)
                .build().toUriString()
        } else if (authentication.principal is OAuth2UserImpl) {
            val oAuth2UserImpl = authentication.principal as OAuth2UserImpl
            val oAuth2UserInfo = oAuth2UserImpl.oAuth2UserInfo
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("email", oAuth2UserInfo.getEmail())
                .queryParam("provider", (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId)
                .build().toUriString()
        } else if (authentication.principal is UserPrincipal && isTargetUrlContainsSignup(targetUrl)) {
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", INVALID_SIGNUP_REQUEST)
                .build().toUriString()
        } else if (authentication.principal is UserPrincipal) {
            val userPrincipal = authentication.principal as UserPrincipal
            val accessToken = jwtUtils.generateToken(userPrincipal)
            val refreshToken = userPrincipal.generateToken()
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString()
        } else {
            UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", BAD_REQUEST)
                .build().toUriString()
        }
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response)
    }

    private fun isTargetUrlContainsLogin(targetUrl: String) = targetUrl.contains("/login")

    private fun isTargetUrlContainsSignup(targetUrl: String) = targetUrl.contains("/signup")

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