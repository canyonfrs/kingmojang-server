package app.kingmojang.fixture

import app.kingmojang.domain.authenticationcode.dto.response.AuthCodeResponse

const val AUTH_CODE_ID = 1L

fun createAuthCodeResponse(
    authCodeId: Long = AUTH_CODE_ID,
    code: Int = AUTH_CODE,
    email: String = EMAIL,
    nickname: String = NICKNAME
): AuthCodeResponse {
    return AuthCodeResponse(authCodeId, code, email, nickname)
}