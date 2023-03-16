package app.kingmojang.domain.memo.dto.response

import app.kingmojang.domain.comment.dto.response.CommentsResponse
import java.time.LocalDateTime

data class MemoResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val isLike: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val fontName: String,
    val fontStyle: String,
    val fontSize: Int,
    val commentsResponse: CommentsResponse,
) {
    companion object {
        fun of(memo: MemoDto, commentsResponse: CommentsResponse): MemoResponse {
            return MemoResponse(
                id = memo.id,
                title = memo.title,
                writer = memo.writer,
                content = memo.content,
                likeCount = memo.likeCount,
                commentCount = memo.commentCount,
                isLike = memo.isLike,
                createdAt = memo.createdAt,
                updatedAt = memo.updatedAt,
                fontName = memo.fontName,
                fontStyle = memo.fontStyle,
                fontSize = memo.fontSize,
                commentsResponse = commentsResponse
            )
        }
    }
}