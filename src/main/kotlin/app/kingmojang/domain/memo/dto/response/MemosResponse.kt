package app.kingmojang.domain.memo.dto.response

import java.time.LocalDateTime

data class MemosResponse(val memos: List<MemoNestedResponse>) {
    companion object {
        fun of(memos: List<MemoDto>): MemosResponse {
            return MemosResponse(memos.stream().map { MemoNestedResponse.of(it) }.toList())
        }
    }
}

data class MemoNestedResponse(
    val id: Long,
    val title: String,
    val writer: String,
    val summary: String,
    val likeCount: Int,
    val commentCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val fontName: String,
    val fontStyle: String,
    val fontSize: Int,
) {
    companion object {
        fun of(memo: MemoDto): MemoNestedResponse {
            return MemoNestedResponse(
                id = memo.id,
                title = memo.title,
                writer = memo.writer,
                summary = memo.content,
                likeCount = memo.likeCount,
                commentCount = memo.commentCount,
                createdAt = memo.createdAt,
                updatedAt = memo.updatedAt,
                fontName = memo.fontName,
                fontStyle = memo.fontStyle,
                fontSize = memo.fontSize,
            )
        }
    }
}