package app.kingmojang.domain.memo.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class MemoRequest(
    @field:NotBlank val title: String,
    @field:NotNull val memberId: Long,
    @field:NotBlank val content: String,
    @field:NotBlank val fontName: String,
    @field:NotBlank val fontStyle: String,
    @field:NotNull val fontSize: Int,
) {
}