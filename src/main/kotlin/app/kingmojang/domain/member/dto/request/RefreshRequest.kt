package app.kingmojang.domain.member.dto.request

import jakarta.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank val refreshToken: String,
    @field:NotBlank val username: String,
) {
}