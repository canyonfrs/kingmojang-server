package app.kingmojang.domain.authenticationcode.domain

import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class AuthenticationCode(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authentication_code_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val recommender: Member,

    val code: Int,

    var isExpired: Boolean,

    val email: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun create(recommender: Member, code: Int, email: String): AuthenticationCode {
            return AuthenticationCode(
                recommender = recommender,
                code = code,
                email = email,
                isExpired = false,
            )
        }
    }

    fun isNotExpired(): Boolean {
        return !this.isExpired
    }

    fun updateToExpire() {
        this.isExpired = true
    }
}