package app.kingmojang.domain.authenticationcode.application

import app.kingmojang.domain.authenticationcode.dto.response.AuthCodeResponse
import app.kingmojang.domain.member.domain.UserPrincipal

interface AuthCodeService {
    fun createAuthCode(userPrincipal: UserPrincipal, email: String): AuthCodeResponse
    fun sendAuthCodeToEmail(response: AuthCodeResponse)
    fun isValidAuthCode(code: Int): Boolean
}