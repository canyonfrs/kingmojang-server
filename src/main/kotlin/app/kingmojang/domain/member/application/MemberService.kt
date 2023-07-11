package app.kingmojang.domain.member.application

import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.dto.request.ChangeNicknameRequest
import app.kingmojang.domain.member.dto.request.ChangePasswordRequest
import app.kingmojang.domain.member.dto.response.ChangeProfileImageResponse
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.exception.SameNicknameAlreadyExistException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.common.InvalidInputException
import app.kingmojang.global.util.S3Utils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val s3Util: S3Utils,
) {

    @Transactional(readOnly = true)
    fun existsNickname(nickname: String, type: String): Boolean {
        if (nickname.isBlank() || !MemberType.isValidate(type)) {
            throw InvalidInputException(nickname)
        }
        return memberRepository.existsByNicknameAndType(nickname, MemberType.valueOf(type))
    }

    @Transactional(readOnly = true)
    fun existsEmail(email: String): Boolean {
        if (email.isBlank()) {
            throw InvalidInputException(email)
        }
        return memberRepository.existsByEmail(email)
    }

    fun changePassword(memberId: Long, request: ChangePasswordRequest) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        member.changePassword(passwordEncoder, request)
    }

    fun changeNickname(memberId: Long, memberType: MemberType, request: ChangeNicknameRequest) {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        if (memberRepository.existsByNicknameAndType(request.nickname, memberType)) {
            throw SameNicknameAlreadyExistException(request.nickname)
        }
        member.changeNickname(request.nickname)
    }

    fun changeProfileImage(memberId: Long, image: MultipartFile?): ChangeProfileImageResponse {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        s3Util.deleteFile(member.profileImage)
        val profileImage = member.changeProfileImage(s3Util, image)
        return ChangeProfileImageResponse(profileImage)
    }
}