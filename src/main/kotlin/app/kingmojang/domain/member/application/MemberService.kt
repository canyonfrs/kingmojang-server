package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.domain.MemberType
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

    fun existsNickname(nickname: String, type: String): Boolean {
        if (isBlank(nickname) || !MemberType.isValidate(type)) {
            throw InvalidInputException(nickname)
        }
        return memberRepository.existsByNicknameAndType(nickname, MemberType.valueOf(type))
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