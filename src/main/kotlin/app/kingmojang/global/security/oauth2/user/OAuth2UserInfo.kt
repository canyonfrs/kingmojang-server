package app.kingmojang.global.security.oauth2.user

abstract class OAuth2UserInfo(
    val attributes: MutableMap<String, Any>,
) {

    abstract fun getId(): String
    abstract fun getEmail(): String
    abstract fun getProfileImage(): String
}