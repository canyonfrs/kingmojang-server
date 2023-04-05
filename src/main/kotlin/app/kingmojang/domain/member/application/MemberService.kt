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
        if (username.isBlank()) {
            throw InvalidInputException(username)
        }
        return memberRepository.existsByUsername(username)
    }

    fun existsNickname(nickname: String, type: String): Boolean {
        if (nickname.isBlank() || !MemberType.isValidate(type)) {
            throw InvalidInputException(nickname)
        }
        return memberRepository.existsByNicknameAndType(nickname, MemberType.valueOf(type))
    }

    fun existsEmail(email: String): Boolean {
        if (email.isBlank()) {
            throw InvalidInputException(email)
        }
        return memberRepository.existsByEmail(email)
    }
}