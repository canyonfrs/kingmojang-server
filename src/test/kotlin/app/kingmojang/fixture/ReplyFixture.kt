package app.kingmojang.fixture

import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.dto.response.ReplyResponse
import java.time.LocalDateTime

const val REPLY_ID: Long = 1L
const val REPLY_CONTENT: String = "답글입니다."
const val REPLY_WRITER: String = "답글 작성자"
const val REPLY_LIKE_COUNT = 0
const val REPLY_IS_LIKE = false
const val REPLY_LIKE_ID = 1L
val REPLY_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val REPLY_UPDATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 42, 52, 128)

fun createReplyRequest(): ReplyRequest {
    return ReplyRequest(MEMBER_ID, REPLY_CONTENT, EMOJI_ID)
}

fun createReplyResponse(
    replyId: Long = REPLY_ID,
    writer: String = REPLY_WRITER,
    content: String = REPLY_CONTENT,
    likeCount: Int = REPLY_LIKE_COUNT,
    isLike: Boolean = REPLY_IS_LIKE,
    createdAt: LocalDateTime = REPLY_CREATED_AT,
    updatedAt: LocalDateTime = REPLY_UPDATED_AT
): ReplyResponse {
    return ReplyResponse(replyId, writer, content, likeCount, isLike, createdAt, updatedAt)
}