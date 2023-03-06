package app.kingmojang.domain.member.api

import app.kingmojang.domain.member.application.MemberService
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/members")
class MemberController(
    private val memberService: MemberService,
) {

    @GetMapping("/username")
    fun existsUsername(@RequestParam(defaultValue = "") username: String): ResponseEntity<CommonResponse<Void>> {
        return existsResult(memberService.existsUsername(username))
    }

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
}