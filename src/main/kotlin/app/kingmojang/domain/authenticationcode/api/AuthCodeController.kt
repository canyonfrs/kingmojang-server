package app.kingmojang.domain.authenticationcode.api

import app.kingmojang.domain.authenticationcode.application.AuthCodeService
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class AuthCodeController(
    private val authCodeService: AuthCodeService,
) {

    @PostMapping("/auth-codes")
    @PreAuthorize("hasRole({'CREATOR', 'ADMIN'})")
    fun createAuthCode(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam email: String
    ): ResponseEntity<CommonResponse<Void>> {
        val response = authCodeService.createAuthCode(userPrincipal, email)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/auth-codes/${response.authCodeId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @GetMapping("/auth-codes")
    fun isValidAuthCode(
        @RequestParam(defaultValue = "0") code: Int
    ): ResponseEntity<CommonResponse<Void>> {
        if (authCodeService.isValidAuthCode(code)) {
            return ResponseEntity.accepted().body(CommonResponse.success())
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(CommonResponse.fail())
    }
}