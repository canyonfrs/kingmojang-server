package app.kingmojang.domain.member.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

class UserPrincipal(
    private val member: Member,
    private val attributes: MutableMap<String, Any>,
) : UserDetails, OAuth2User {
    private var enabled = !member.deleted

    companion object {
        fun create(member: Member): UserPrincipal {
            return UserPrincipal(member, mutableMapOf())
        }

        fun create(member: Member, attributes: MutableMap<String, Any>): UserPrincipal {
            return UserPrincipal(member, attributes)
        }
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(member.type.value))
    }

    fun getId(): Long = member.id!!

    fun getNickname(): String = member.nickname

    fun getProvider(): String = member.provider.toString()

    fun generateToken(): String = member.generateToken()

    override fun getName(): String = member.nickname

    override fun getAttributes(): MutableMap<String, Any> = this.attributes

    override fun getPassword(): String = member.password

    override fun getUsername(): String = member.email

    override fun isAccountNonExpired(): Boolean = enabled

    override fun isAccountNonLocked(): Boolean = enabled

    override fun isCredentialsNonExpired(): Boolean = enabled

    override fun isEnabled(): Boolean = enabled
}