package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.common.InvalidInputException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
) {

    fun existsUsername(username: String): Boolean {
        if (isBlank(username)) {
            throw InvalidInputException(username)
        }
        return memberRepository.existsByUsername(username)
    }

    fun existsNickname(nickname: String): Boolean {
        if (isBlank(nickname)) {
            throw InvalidInputException(nickname)
        }
        return memberRepository.existsByNickname(nickname)
    }

    fun existsEmail(email: String): Boolean {
        if (isBlank(email)) {
            throw InvalidInputException(email)
        }
        return memberRepository.existsByEmail(email)
    }

    private fun isBlank(value: String): Boolean {
        return value.isBlank()
    }
}