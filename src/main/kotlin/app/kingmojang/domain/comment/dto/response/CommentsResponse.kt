package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Comment
import java.time.LocalDateTime

data class CommentsResponse(val comments: List<CommentResponse>) {
    companion object {
        fun of(commentResponse: List<CommentResponse>): CommentsResponse {
            return CommentsResponse(commentResponse)
        }
    }
}

data class CommentResponse(
    val commentId: Long,
    val writer: String,
    val content: String,
    val likeCount: Int = 0,
    val replyCount: Int = 0,
    val isLike: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val replies: MutableList<ReplyResponse>,
) {
    companion object {
        fun of(comment: Comment): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                writer = comment.writer.nickname,
                content = comment.content,
                likeCount = comment.likeCount,
                replyCount = comment.replyCount,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                replies = mutableListOf()
            )
        }

        fun of(commentId: Long): CommentResponse {
            return CommentResponse(
                commentId = commentId,
                writer = "",
                content = "",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                replies = mutableListOf()
            )
        }

        fun of(comment: Comment, isLike: Boolean): CommentResponse {
            return CommentResponse(
                commentId = comment.id!!,
                writer = comment.writer.nickname,
                content = comment.content,
                likeCount = comment.likeCount,
                replyCount = comment.replyCount,
                isLike = isLike,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                replies = mutableListOf()
            )
        }
    }
}
