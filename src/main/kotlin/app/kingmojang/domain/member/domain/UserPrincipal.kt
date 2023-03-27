package app.kingmojang.domain.member.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(val member: Member) : UserDetails {

    private var enabled = !member.deleted

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(member.type.value))
    }

    override fun getPassword(): String = member.password

    override fun getUsername(): String = member.username

    override fun isAccountNonExpired(): Boolean = enabled

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = enabled

    override fun isEnabled(): Boolean = enabled
}