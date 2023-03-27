package app.kingmojang.global.security.oauth2.user

class TwitchOAuth2UserInfo(attributes: MutableMap<String, Any>) : OAuth2UserInfo(attributes) {
    override fun getId(): String {
        return attributes["id"].toString()
    }

    override fun getEmail(): String {
        return attributes["email"].toString()
    }

    override fun getProfileImage(): String {
        return attributes["profile_image_url"].toString()
    }
}
