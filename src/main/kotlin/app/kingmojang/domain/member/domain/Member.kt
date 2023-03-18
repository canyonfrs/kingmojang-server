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
    val isAuthorizedAccount: Boolean,

    @Column(name = "profile_image")
    var profileImage: String,

    @Column(name = "follower_count")
    var followerCount: Int = 0,

    @Embedded
    var information: CreatorInformation,

    @Embedded
    var refreshToken: RefreshToken,

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
                information = CreatorInformation(
                    request.introduce,
                    request.youtube,
                    request.broadcastLink,
                    request.donationLink
                ),
                type = MemberType.valueOf(request.memberType),
                isAuthorizedAccount = request.isAuthorizedAccount,
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
}

@Embeddable
data class CreatorInformation(
    @Lob
    @Column
    val introduce: String?,

    @Column
    val youtube: String?,

    @Column(name = "broadcast_link")
    val broadcastLink: String?,

    @Column(name = "donation_link")
    val donationLink: String?,
) {
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