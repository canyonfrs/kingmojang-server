package app.kingmojang.domain.member.dto.response

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
) {
}