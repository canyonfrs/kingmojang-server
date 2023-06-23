package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.highlight.dto.response.HighlightResponse
import java.time.LocalDateTime

data class CommentResponse(
    val commentId: Long,
    val writer: String,
    val content: String,
    val highlight: HighlightResponse?,
    val likeCount: Int,
    val replyCount: Int,
    val deleted: Boolean,
    val isLike: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(comment: Comment, isLike: Boolean): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                writer = comment.writer.nickname,
                content = comment.content,
                highlight = comment.highlight?.let { HighlightResponse.of(it) },
                likeCount = comment.likeCount,
                replyCount = comment.replyCount,
                deleted = comment.deleted,
                isLike = isLike,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
