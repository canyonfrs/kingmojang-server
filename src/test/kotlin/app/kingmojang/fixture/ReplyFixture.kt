package app.kingmojang.fixture

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.dto.response.ReplyResponse
import app.kingmojang.domain.like.domain.ReplyLike
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Memo
import java.time.LocalDateTime

const val REPLY_ID: Long = 1L
const val REPLY_CONTENT: String = "답글입니다."
const val REPLY_WRITER: String = "답글 작성자"
const val REPLY_LIKE_COUNT = 0
const val REPLY_DELETED = false
const val REPLY_IS_LIKE = false
const val REPLY_LIKE_ID = 1L
val REPLY_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val REPLY_LIKE_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val REPLY_UPDATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 42, 52, 128)

fun createReplyRequest(
    content: String = REPLY_CONTENT,
    emojiId: Long = EMOJI_ID
): ReplyRequest {
    return ReplyRequest(content, emojiId)
}

fun createReplyResponse(
    replyId: Long = REPLY_ID,
    writer: String = REPLY_WRITER,
    content: String = REPLY_CONTENT,
    likeCount: Int = REPLY_LIKE_COUNT,
    deleted: Boolean = REPLY_DELETED,
    isLike: Boolean = REPLY_IS_LIKE,
    createdAt: LocalDateTime = REPLY_CREATED_AT,
    updatedAt: LocalDateTime = REPLY_UPDATED_AT
): ReplyResponse {
    return ReplyResponse(replyId, writer, content, likeCount, deleted, isLike, createdAt, updatedAt)
}

fun createReply(
    replyId: Long = REPLY_ID,
    writer: Member = createMember(),
    comment: Comment = createComment(),
    memo: Memo = createMemo(),
    content: String = REPLY_CONTENT,
    likeCount: Int = REPLY_LIKE_COUNT,
    createdAt: LocalDateTime = REPLY_CREATED_AT,
    updatedAt: LocalDateTime = REPLY_UPDATED_AT
): Reply {
    return Reply(replyId, writer, comment, memo, content, likeCount, createdAt, updatedAt)
}

fun createReplyLike(
    replyLikeId: Long = REPLY_LIKE_ID,
    member: Member = createMember(),
    reply: Reply = createReply(),
    createdAt: LocalDateTime = REPLY_LIKE_CREATED_AT,
): ReplyLike {
    return ReplyLike(replyLikeId, member, reply, createdAt)
}