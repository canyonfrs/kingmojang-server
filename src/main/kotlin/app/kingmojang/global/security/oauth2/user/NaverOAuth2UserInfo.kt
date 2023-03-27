package app.kingmojang.global.security.oauth2.user

class NaverOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return getResponse()["id"].toString()
    }

    override fun getEmail(): String {
        return getResponse()["email"].toString()
    }

    override fun getProfileImage(): String {
        return getResponse()["profile_image"].toString()
    }

    private fun getResponse() = (attributes["response"] as Map<String, Any>)
}
