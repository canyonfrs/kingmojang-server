package app.kingmojang.domain.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignupRequest(
    @field:NotBlank val username: String,
    val password: String = "",
    @field:NotBlank val nickname: String,
    @field:Email val email: String,
    @field:NotBlank val provider: String,
    @field:NotBlank val memberType: String,
    val authCode: Int,
) {
}