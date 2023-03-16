package app.kingmojang.domain.memo.dto.response

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class MemoDto @QueryProjection constructor(
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
) {
}