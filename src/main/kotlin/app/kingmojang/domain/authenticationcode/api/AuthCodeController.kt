package app.kingmojang.domain.authenticationcode.api

import app.kingmojang.domain.authenticationcode.application.AuthCodeService
import app.kingmojang.domain.member.domain.UserPrincipal
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    ): ResponseEntity<Void> {
        val response = authCodeService.createAuthCode(userPrincipal, email)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/auth-codes/${response.authCodeId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).build()
    }
}