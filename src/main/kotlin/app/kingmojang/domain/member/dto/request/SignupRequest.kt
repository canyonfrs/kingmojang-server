package app.kingmojang.domain.member.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SignupRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val password: String,
    @field:NotBlank val nickname: String,
    @field:Email val email: String,
    @field:NotNull val isAuthorizedAccount: Boolean,
    @field:NotBlank val provider: String,
    val introduce: String?,
    val youtube: String?,
    val broadcastLink: String?,
    val donationLink: String?,
    @field:NotBlank val memberType: String,
) {
}