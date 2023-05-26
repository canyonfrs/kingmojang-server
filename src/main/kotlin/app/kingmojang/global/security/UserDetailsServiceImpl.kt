package app.kingmojang.global.security

import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundEmailException
import app.kingmojang.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        return memberRepository.findByEmail(email)?.let {
            UserPrincipal.create(it)
        } ?: throw NotFoundEmailException(email)
    }
}