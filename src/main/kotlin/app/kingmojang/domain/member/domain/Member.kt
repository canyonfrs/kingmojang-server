package app.kingmojang.domain.member.domain

import app.kingmojang.domain.member.dto.request.SignupRequest
import jakarta.persistence.*
import java.time.LocalDateTime

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

    @Embedded
    var information: CreatorInformation,

    @Enumerated(EnumType.STRING)
    val memberType: MemberType,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
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
                memberType = request.memberType,
                isAuthorizedAccount = request.memberType == MemberType.CREATOR,
                profileImage = "",
                createdAt = LocalDateTime.now()
            )
        }
    }
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