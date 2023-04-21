package app.kingmojang.domain.member.domain

import app.kingmojang.domain.member.dto.request.SignupRequest
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "username_uk", columnNames = ["username"]),
        UniqueConstraint(name = "email_uk", columnNames = ["email"])
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long? = null,

    @Column(nullable = false)
    val username: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var nickname: String,

    @Column(nullable = false)
    val email: String,

    @Column(name = "profile_image")
    var profileImage: String,

    @Column(name = "follower_count")
    var followerCount: Int = 0,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_information_id")
    var creatorInformation: CreatorInformation? = null,

    @Embedded
    var refreshToken: RefreshToken,

    @Enumerated(EnumType.STRING)
    val provider: AuthProvider,

    @Enumerated(EnumType.STRING)
    val type: MemberType,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null,

    var deleted: Boolean = false,
) {
    companion object {
        fun create(request: SignupRequest): Member {
            return Member(
                username = request.username,
                nickname = request.nickname,
                password = request.password,
                email = request.email,
                provider = AuthProvider.valueOf(request.provider),
                type = MemberType.valueOf(request.memberType),
                profileImage = "",
                createdAt = LocalDateTime.now(),
                refreshToken = RefreshToken("", LocalDateTime.now())
            )
        }
    }

    fun generateToken(): String {
        refreshToken = RefreshToken.create()
        return refreshToken.value
    }

    fun increaseFollowerCount() = this.followerCount++

    fun decreaseFollowerCount() = this.followerCount--

    fun createCreatorInformation(creatorInformation: CreatorInformation) {
        this.creatorInformation = creatorInformation
    }
}

@Embeddable
data class RefreshToken(
    @Column(name = "refresh_token")
    val value: String,

    @Column(name = "token_expired_date")
    val expiredDate: LocalDateTime,
) {
    companion object {
        fun create(): RefreshToken {
            return RefreshToken(
                value = UUID.randomUUID().toString(),
                expiredDate = LocalDateTime.now().plusYears(1)
            )
        }
    }

    fun isValid(refreshToken: String): Boolean {
        return refreshToken == value && expiredDate.isAfter(LocalDateTime.now())
    }
}