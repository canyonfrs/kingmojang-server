package app.kingmojang.global.security.oauth2.user

class GoogleOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["sub"].toString()
    }

    override fun getEmail(): String {
        return attributes["email"].toString()
    }

    override fun getProfileImage(): String {
        return attributes["picture"].toString()
    }
}