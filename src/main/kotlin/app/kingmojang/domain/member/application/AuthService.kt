package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.repository.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @Transactional
    fun signup(request: SignupRequest): Long {
        val member = Member.create(request.copy(password = passwordEncoder.encode(request.password)))
        return memberRepository.save(member).id!!
    }

}