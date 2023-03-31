package app.kingmojang.domain.comment.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CommentRequest(
    @field:NotNull val memberId: Long,
    @field:NotBlank val content: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val emojiId: Long,
)
