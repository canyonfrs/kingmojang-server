package app.kingmojang.domain.comment.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank

data class CommentRequest(
    @field:NotBlank val username: String,
    @field:NotBlank val content: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val emojiId: Long,
)
