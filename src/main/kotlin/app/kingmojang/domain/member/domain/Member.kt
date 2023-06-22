package app.kingmojang.domain.member.domain

import app.kingmojang.domain.SoftDeletable
import app.kingmojang.domain.member.dto.request.ChangePasswordRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.exception.InvalidPasswordException
import app.kingmojang.global.exception.common.FileTypeException
import app.kingmojang.global.util.S3Utils
import jakarta.persistence.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "email_uk", columnNames = ["email"])
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    val id: Long? = null,

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
) : SoftDeletable() {
    companion object {
        fun create(request: SignupRequest): Member {
            return Member(
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
        if (!isCreator()) {
            throw IllegalStateException("Member is not creator")
        }
        this.creatorInformation = creatorInformation
    }

    fun withdraw() {
        this.delete()
    }

    fun changePassword(passwordEncoder: PasswordEncoder, request: ChangePasswordRequest) {
        if (this.password.isBlank() || passwordEncoder.matches(request.oldPassword, this.password)) {
            this.password = passwordEncoder.encode(request.newPassword)
        }
        throw InvalidPasswordException()
    }

    fun changeNickname(newNickname: String) {
        this.nickname = newNickname
    }

    fun changeProfileImage(s3Util: S3Utils, image: MultipartFile?): String {
        val fileName = "${UUID.nameUUIDFromBytes(email.toByteArray())}/profile"
        if (image == null || image.isEmpty) {
            this.profileImage = ""
        } else if (canUploadImage(image)) {
            this.profileImage = s3Util.uploadFile(fileName, image)
        } else {
            throw FileTypeException(image.originalFilename ?: "Image")
        }
        return this.profileImage
    }

    fun isCreator() = this.type == MemberType.CREATOR

    private fun canUploadImage(image: MultipartFile) =
        (image.contentType == "image/jpeg" || image.contentType == "image/png" || image.contentType == "image/gif")
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