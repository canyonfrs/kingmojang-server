package app.kingmojang.domain.authenticationcode.application

import app.kingmojang.domain.authenticationcode.dto.response.AuthCodeResponse
import app.kingmojang.domain.member.domain.UserPrincipal
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service

@Primary
@Service
class AuthCodeServiceDecorator(
    @Qualifier("AuthCodeServiceImpl") private val authCodeService: AuthCodeService,
) : AuthCodeService {

    override fun createAuthCode(userPrincipal: UserPrincipal, email: String): AuthCodeResponse {
        val response = authCodeService.createAuthCode(userPrincipal, email)
        sendAuthCodeToEmail(response)
        return response
    }

    @Async
    override fun sendAuthCodeToEmail(response: AuthCodeResponse) {
        authCodeService.sendAuthCodeToEmail(response)
    }

    override fun isValidAuthCode(code: Int): Boolean = authCodeService.isValidAuthCode(code)
}