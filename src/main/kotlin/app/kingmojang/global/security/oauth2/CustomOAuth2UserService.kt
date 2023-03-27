package app.kingmojang.global.security.oauth2

import app.kingmojang.domain.member.domain.AuthProvider
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.security.oauth2.user.OAuth2UserInfo
import app.kingmojang.global.security.oauth2.user.OAuth2UserInfoFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val memberRepository: MemberRepository,
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    override fun loadUser(request: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(request)
        val attributes = when (request.clientRegistration.registrationId) {
            AuthProvider.TWITCH.value -> findTwitchUserInfoAttributes(oAuth2User, request)
            else -> oAuth2User.attributes
        }
        val oAuth2UserInfo: OAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
            request.clientRegistration.registrationId,
            attributes
        )

        return memberRepository.findByEmail(oAuth2UserInfo.getEmail())?.let {
            UserPrincipal(it, attributes)
        } ?: OAuth2UserImpl(oAuth2UserInfo)
    }

    private fun findTwitchUserInfoAttributes(oAuth2User: OAuth2User, request: OAuth2UserRequest) =
        (TwitchUserInfoFinder.getUserInformation(oAuth2User, request)["data"] as List<MutableMap<String, Any>>)[0]
}

class OAuth2UserImpl(
    val oAuth2UserInfo: OAuth2UserInfo,
) : OAuth2User {

    override fun getName(): String = this.oAuth2UserInfo.getEmail()

    override fun getAttributes(): MutableMap<String, Any> = this.oAuth2UserInfo.attributes

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority("ROLE_GUEST"))
}