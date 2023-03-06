package app.kingmojang.global.security

import app.kingmojang.domain.member.domain.UserDetailsImpl
import app.kingmojang.domain.member.exception.NotFoundUsernameException
import app.kingmojang.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        return memberRepository.findByUsername(username)?.let {
            UserDetailsImpl(it)
        } ?: throw NotFoundUsernameException(username)
    }
}