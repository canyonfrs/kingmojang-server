package app.kingmojang.global.security.oauth2.user

class KakaoOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["id"].toString()
    }

    override fun getEmail(): String {
        return getKakaoAccount()["email"].toString()
    }

    override fun getProfileImage(): String {
        return getProfile()["thumbnail_image_url"].toString()
    }

    private fun getKakaoAccount() = (attributes["kakao_account"] as Map<String, Any>)

    private fun getProfile() = (getKakaoAccount()["profile"] as Map<String, Any>)
}
