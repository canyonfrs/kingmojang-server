package app.kingmojang.domain.memo.dto.response

import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.memo.domain.Memo
import java.time.LocalDateTime

data class MemoResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val content: String,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val fontName: String,
    val fontStyle: String,
    val fontSize: Int,
    val isFollow: Boolean,
    val isLike: Boolean,
    val commentsResponse: CommentsResponse
) {
    companion object {
        fun of(memo: Memo, isLike: Boolean, isFollow: Boolean, commentsResponse: CommentsResponse): MemoResponse {
            return MemoResponse(
                memo.id!!,
                memo.title,
                memo.writer.nickname,
                memo.content,
                memo.likeCount,
                memo.commentCount,
                memo.createdAt,
                memo.updatedAt,
                memo.font.name,
                memo.font.style,
                memo.font.size,
                isFollow,
                isLike,
                commentsResponse
            )
        }
    }
}