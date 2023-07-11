package app.kingmojang.domain.member.api

import app.kingmojang.domain.member.application.MemberService
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.dto.request.ChangeNicknameRequest
import app.kingmojang.domain.member.dto.request.ChangePasswordRequest
import app.kingmojang.domain.member.dto.response.ChangeProfileImageResponse
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
    private val memoService: MemoService,
) {

    @GetMapping("/nickname")
    fun existsNickname(
        @RequestParam(defaultValue = "") nickname: String,
        @RequestParam(defaultValue = "USER") type: String,
    ): ResponseEntity<CommonResponse<Void>> {
        return existsResult(memberService.existsNickname(nickname, type))
    }

    @GetMapping("/email")
    fun existsEmail(@RequestParam(defaultValue = "") email: String): ResponseEntity<CommonResponse<Void>> {
        return existsResult(memberService.existsEmail(email))
    }

    private fun existsResult(isExists: Boolean) = if (isExists) {
        ResponseEntity.status(HttpStatus.CONFLICT).body(CommonResponse.success())
    } else {
        ResponseEntity.noContent().build()
    }

    @PutMapping("/{memberId}/password")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun changePassword(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
        @RequestBody request: ChangePasswordRequest,
    ): ResponseEntity<CommonResponse<Void>> {
        if (!userPrincipal.isValidMember(memberId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        memberService.changePassword(memberId, request)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{memberId}/nickname")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun changeNickname(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
        @RequestBody request: ChangeNicknameRequest,
    ): ResponseEntity<CommonResponse<Void>> {
        if (!userPrincipal.isValidMember(memberId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        memberService.changeNickname(memberId, userPrincipal.getMemberType(), request)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/{memberId}/profile-image")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun changeProfileImage(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
        @RequestPart(required = false) image: MultipartFile?,
    ): ResponseEntity<CommonResponse<ChangeProfileImageResponse>> {
        if (!userPrincipal.isValidMember(memberId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        val response = memberService.changeProfileImage(memberId, image)
        return ResponseEntity.ok(CommonResponse.success(response))
    }
}