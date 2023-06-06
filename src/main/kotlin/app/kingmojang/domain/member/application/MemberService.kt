package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.dto.request.ChangeNicknameRequest
import app.kingmojang.domain.member.dto.request.ChangePasswordRequest
import app.kingmojang.domain.member.dto.response.ChangeProfileImageResponse
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.exception.SameNicknameAlreadyExistException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.common.FileTypeException
import app.kingmojang.global.exception.common.InvalidInputException
import app.kingmojang.global.util.S3Utils
import app.kingmojang.global.validator.MemberIdValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional(readOnly = true)
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val s3Util: S3Utils,
) {

    fun existsNickname(nickname: String, type: String): Boolean {
        if (nickname.isBlank() || !MemberType.isValidate(type)) {
            throw InvalidInputException(nickname)
        }
        return memberRepository.existsByNicknameAndType(nickname, MemberType.valueOf(type))
    }

    fun existsEmail(email: String): Boolean {
        if (email.isBlank()) {
            throw InvalidInputException(email)
        }
        return memberRepository.existsByEmail(email)
    }

    @Transactional
    fun changePassword(userPrincipal: UserPrincipal, memberId: Long, request: ChangePasswordRequest) {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(userPrincipal.getId()) ?: throw NotFoundMemberException(memberId)
        member.changePassword(passwordEncoder, request)
    }

    @Transactional
    fun changeNickname(userPrincipal: UserPrincipal, memberId: Long, request: ChangeNicknameRequest) {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(userPrincipal.getId()) ?: throw NotFoundMemberException(memberId)
        if (memberRepository.existsByNicknameAndType(request.nickname, userPrincipal.getMemberType())) {
            throw SameNicknameAlreadyExistException(request.nickname)
        }
        member.changeNickname(request.nickname)
    }

    @Transactional
    fun changeProfileImage(
        userPrincipal: UserPrincipal,
        memberId: Long,
        image: MultipartFile?
    ): ChangeProfileImageResponse {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(userPrincipal.getId()) ?: throw NotFoundMemberException(memberId)
        val fileName = "${userPrincipal.username}/profile"
        if (member.profileImage.isNotBlank()) {
            s3Util.deleteFile(fileName)
        }
        return if (image == null || image.isEmpty) {
            val emptyProfileImage = ""
            member.changeProfileImage(emptyProfileImage)
            ChangeProfileImageResponse(emptyProfileImage)
        } else if (canUploadImage(image)) {
            val profileImage = s3Util.uploadFile(fileName, image)
            member.changeProfileImage(profileImage)
            ChangeProfileImageResponse(profileImage)
        } else {
            throw FileTypeException(image.originalFilename ?: "Image")
        }
    }

    private fun canUploadImage(image: MultipartFile) =
        (image.contentType == "image/jpeg" || image.contentType == "image/png" || image.contentType == "image/gif")
}