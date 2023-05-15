package app.kingmojang.fixture

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentResponse
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.dto.response.ReplyResponse
import app.kingmojang.domain.like.domain.CommentLike
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Memo
import java.time.LocalDateTime

const val COMMENT_ID = 1L
const val COMMENT_WRITER = "댓글 쓴 사람"
const val COMMENT_CONTENT = "안녕하세요. 오늘은 무슨 댓글을 달아볼까요??"
const val COMMENT_LIKE_COUNT = 0
const val REPLY_COUNT = 2
const val COMMENT_IS_LIKE = false
const val COMMENT_LIKE_ID = 1L
val COMMENT_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val COMMENT_LIKE_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val COMMENT_UPDATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 42, 52, 128)

fun createCommentRequest(
    memberId: Long = MEMBER_ID,
    content: String = COMMENT_CONTENT,
    emojiId: Long = EMOJI_ID
): CommentRequest {
    return CommentRequest(
        memberId,
        content,
        emojiId
    )
}

fun createCommentResponse(
    commentId: Long = COMMENT_ID,
    writer: String = COMMENT_WRITER,
    content: String = COMMENT_CONTENT,
    likeCount: Int = COMMENT_LIKE_COUNT,
    replyCount: Int = REPLY_COUNT,
    isLike: Boolean = COMMENT_IS_LIKE,
    createdAt: LocalDateTime = COMMENT_CREATED_AT,
    updatedAt: LocalDateTime = COMMENT_UPDATED_AT,
    replies: MutableList<ReplyResponse> = mutableListOf(createReplyResponse(), createReplyResponse())
): CommentResponse {
    return CommentResponse(commentId, writer, content, likeCount, replyCount, isLike, createdAt, updatedAt, replies)
}

fun createCommentsResponse(): CommentsResponse {
    return CommentsResponse.of(listOf(createCommentResponse(), createCommentResponse(), createCommentResponse()))
}

fun createComment(
    commentId: Long = COMMENT_ID,
    writer: Member = createMember(),
    memo: Memo = createMemo(),
    content: String = COMMENT_CONTENT,
    likeCount: Int = COMMENT_LIKE_COUNT,
    replyCount: Int = REPLY_COUNT,
    createdAt: LocalDateTime = COMMENT_CREATED_AT,
    updatedAt: LocalDateTime = COMMENT_UPDATED_AT,
): Comment {
    return Comment(commentId, writer, memo, content, likeCount, replyCount, createdAt, updatedAt)
}

fun createCommentLike(
    commentLikeId: Long = COMMENT_LIKE_ID,
    member: Member = createMember(),
    comment: Comment = createComment(),
    createdAt: LocalDateTime = COMMENT_LIKE_CREATED_AT
): CommentLike {
    return CommentLike(commentLikeId, member, comment, createdAt)
}