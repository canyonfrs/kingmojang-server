package app.kingmojang.domain.comment.dto.response

import app.kingmojang.domain.comment.domain.Reply
import java.time.LocalDateTime

data class ReplyResponse(
    val id: Long,
    val writer: String,
    val content: String,
    val likeCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(r: Reply): ReplyResponse {
            return ReplyResponse(
                id = r.id!!,
                writer = r.writer.nickname,
                content = r.content,
                likeCount = r.likeCount,
                createdAt = r.createdAt,
                updatedAt = r.updatedAt
            )
        }
    }
}