package app.kingmojang.domain.authenticationcode.dto.response

import app.kingmojang.domain.authenticationcode.domain.AuthenticationCode

data class AuthCodeResponse(
    val authCodeId: Long,
    val code: Int,
    val email: String,
    val nickname: String,
) {
    companion object {
        fun of(authCode: AuthenticationCode, nickname: String): AuthCodeResponse {
            return AuthCodeResponse(
                authCodeId = authCode.id!!,
                code = authCode.code,
                email = authCode.email,
                nickname = nickname,
            )
        }
    }
}
