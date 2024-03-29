package app.kingmojang.domain.member.api

import app.kingmojang.domain.member.application.AuthService
import app.kingmojang.domain.member.dto.request.LoginRequest
import app.kingmojang.domain.member.dto.request.RefreshRequest
import app.kingmojang.domain.member.dto.request.SignupRequest
import app.kingmojang.domain.member.dto.response.TokenResponse
import app.kingmojang.global.common.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/members")
    fun signup(@RequestBody @Valid request: SignupRequest): ResponseEntity<CommonResponse<Void>> {
        val id = authService.signup(request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/members/{id}")
            .buildAndExpand(id).toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<CommonResponse<TokenResponse>> {
        val tokenResponse = authService.login(request)
        return ResponseEntity.ok(CommonResponse.success(tokenResponse))
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody @Valid request: RefreshRequest): ResponseEntity<CommonResponse<TokenResponse>> {
        val tokenResponse = authService.refresh(request)
        return ResponseEntity.ok(CommonResponse.success(tokenResponse))
    }
}