package app.kingmojang.domain.comment.dto.request

import app.kingmojang.domain.highlight.dto.request.HighlightRequest
import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.NotBlank

data class CommentRequest(
    @field:NotBlank val content: String,
    @JsonInclude(JsonInclude.Include.NON_NULL) val emojiId: Long,
    val highlight: HighlightRequest?,
)
