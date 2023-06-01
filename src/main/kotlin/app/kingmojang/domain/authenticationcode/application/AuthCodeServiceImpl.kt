package app.kingmojang.domain.authenticationcode.application

import app.kingmojang.domain.authenticationcode.domain.AuthenticationCode
import app.kingmojang.domain.authenticationcode.dto.response.AuthCodeResponse
import app.kingmojang.domain.authenticationcode.exception.UnauthorizedCreateAuthCodeException
import app.kingmojang.domain.authenticationcode.repository.AuthCodeRepository
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.ThreadLocalRandom

@Service
@Qualifier("AuthCodeServiceImpl")
class AuthCodeServiceImpl(
    private val mailSender: JavaMailSender,
    private val authCodeRepository: AuthCodeRepository,
    private val memberRepository: MemberRepository,
) : AuthCodeService {
    companion object {
        const val SUBJECT = "킹모장 초대코드 발송"
        const val CODE_ORIGIN = 0
        const val CODE_LIMIT_BOUND = 9999_9999
    }

    @Transactional
    override fun createAuthCode(userPrincipal: UserPrincipal, email:String): AuthCodeResponse {
        val memberId = userPrincipal.getId()
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        if (member.type == MemberType.USER) {
            throw UnauthorizedCreateAuthCodeException(memberId)
        }
        val code = makeRandomCode()
        val authCode = authCodeRepository.save(AuthenticationCode.create(member, code, email))
        return AuthCodeResponse.of(authCode, member.nickname)
    }

    private fun makeRandomCode(): Int {
        var code = ThreadLocalRandom.current().nextInt(CODE_ORIGIN, CODE_LIMIT_BOUND)
        while (authCodeRepository.existsByCodeAndIsExpired(code, false)) {
            code = ThreadLocalRandom.current().nextInt(CODE_ORIGIN, CODE_LIMIT_BOUND)
        }
        return code
    }

    @Async
    override fun sendAuthCodeToEmail(response: AuthCodeResponse) {
        val message = SimpleMailMessage()
        message.setTo(response.email)
        message.subject = SUBJECT
        message.text = String.format("%08d", response.code)
        mailSender.send(message)
    }

    override fun isValidAuthCode(code: Int): Boolean {
        val authenticationCode = authCodeRepository.findByCode(code) ?: return false
        return authenticationCode.isNotExpired()
    }
}