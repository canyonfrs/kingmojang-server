package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Reply
import java.time.LocalDateTime

data class ReplyResponse(
    val id: Long,
    val writer: String,
    val content: String,
    val likeCount: Int,
    val deleted: Boolean,
    val isLike: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(reply: Reply, isLike: Boolean): ReplyResponse {
            return ReplyResponse(
                id = reply.id!!,
                writer = reply.writer.nickname,
                content = reply.content,
                deleted = reply.deleted,
                isLike = isLike,
                likeCount = reply.likeCount,
                createdAt = reply.createdAt,
                updatedAt = reply.updatedAt,
            )
        }
    }
}