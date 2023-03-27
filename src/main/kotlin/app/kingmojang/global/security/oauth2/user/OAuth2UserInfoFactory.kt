package app.kingmojang.global.security.oauth2.user

import app.kingmojang.domain.member.domain.AuthProvider

class OAuth2UserInfoFactory {
    companion object {
        fun getOAuth2UserInfo(registrationId: String, attributes: MutableMap<String, Any>): OAuth2UserInfo {
            return when (registrationId) {
                AuthProvider.GOOGLE.value -> GoogleOAuth2UserInfo(attributes)
                AuthProvider.KAKAO.value -> KakaoOAuth2UserInfo(attributes)
                AuthProvider.NAVER.value -> NaverOAuth2UserInfo(attributes)
                AuthProvider.TWITCH.value -> TwitchOAuth2UserInfo(attributes)
                else -> throw RuntimeException()
            }
        }
    }
}