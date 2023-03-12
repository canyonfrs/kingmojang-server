package app.kingmojang.domain.memo.dto.response

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
) {
    companion object {
        fun of(memo: MemoDto): MemoResponse {
            return MemoResponse(
                id = memo.id,
                title = memo.title,
                writer = memo.writer,
                content = memo.content,
                likeCount = memo.likeCount,
                commentCount = memo.commentCount,
                createdAt = memo.createdAt,
                updatedAt = memo.updatedAt,
                fontName = memo.fontName,
                fontStyle = memo.fontStyle,
                fontSize = memo.fontSize,
            )
        }

        fun of(memo: Memo): MemoResponse {
            return MemoResponse(
                id = memo.id!!,
                title = memo.title,
                writer = memo.writer.nickname,
                content = memo.content,
                likeCount = memo.likeCount,
                commentCount = memo.commentCount,
                createdAt = memo.createdAt,
                updatedAt = memo.updatedAt,
                fontName = memo.font.name,
                fontStyle = memo.font.style,
                fontSize = memo.font.size,
            )
        }
    }
}