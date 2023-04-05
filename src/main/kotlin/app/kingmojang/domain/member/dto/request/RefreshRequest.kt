package app.kingmojang.domain.member.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class RefreshRequest(
    @field:NotBlank val refreshToken: String,
    @field:NotNull val memberId: Long,
) {
}