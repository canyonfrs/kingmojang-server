package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.QComment
import app.kingmojang.domain.comment.domain.QReply
import app.kingmojang.domain.comment.dto.response.*
import app.kingmojang.domain.like.domain.QCommentLike
import app.kingmojang.domain.like.domain.QReplyLike
import app.kingmojang.global.common.request.CommonPageRequest
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CommentQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun readCommentsInMemo(memoId: Long, request: CommonPageRequest): List<CommentResponse> {
        val comment = QComment.comment
        val reply = QReply.reply

        val result = findAllByMemoIdWithPagination(memoId, request)
        val commentsMap = mutableMapOf<Long, CommentResponse>()

        result.forEach {
            val c = it.get(comment)
            val r = it.get(reply)
            if (c != null && r != null) {
                val commentResponse = commentsMap.getOrDefault(c.id!!, CommentResponse.of(c))
                commentResponse.replies.add(ReplyResponse.of(r))
                commentsMap[c.id!!] = commentResponse
            } else if (c != null) {
                commentsMap[c.id!!] = commentsMap.getOrDefault(c.id!!, CommentResponse.of(c))
            } else if (r != null) {
                val nullableCommentId = r.comment.id!!
                val commentResponse = commentsMap.getOrDefault(nullableCommentId, CommentResponse.of(nullableCommentId))
                commentResponse.replies.add(ReplyResponse.of(r))
                commentsMap[nullableCommentId] = commentResponse
            }
        }
        return commentsMap.map { it.value }.toList()
    }

    fun readCommentsInMemoWithUsername(
        memoId: Long,
        username: String,
        request: CommonPageRequest,
    ): List<CommentResponse> {
        val comment = QComment.comment
        val reply = QReply.reply

        val result = findAllByMemoIdWithPagination(memoId, request)

        val commentIds = result.filter { it.get(comment) != null }.map { it.get(comment)?.id!! }.toSet()
        val replyIds = result.filter { it.get(reply) != null }.map { it.get(reply)?.id!! }.toSet()

        val commentLikeResult = findAllByCommentIdsAndMemberUsername(commentIds, username).toSet()
        val replyLikeResult = findAllByReplyIdsAndMemberUsername(replyIds, username).toSet()
        val commentsMap = mutableMapOf<Long, CommentResponse>()

        result.forEach {
            val c = it.get(comment)
            val r = it.get(reply)
            if (c != null && r != null) {
                val commentId = c.id!!
                val commentResponse =
                    commentsMap.getOrDefault(commentId, CommentResponse.of(c, (commentLikeResult.contains(commentId))))
                commentResponse.replies.add(ReplyResponse.of(r, (replyLikeResult.contains(r.id!!))))
                commentsMap[commentId] = commentResponse
            } else if (c != null) {
                val commentId = c.id!!
                commentsMap[commentId] =
                    commentsMap.getOrDefault(commentId, CommentResponse.of(c, commentLikeResult.contains(commentId)))
            } else if (r != null) {
                val nullableCommentId = r.comment.id!!
                val commentResponse = commentsMap.getOrDefault(nullableCommentId, CommentResponse.of(nullableCommentId))
                commentResponse.replies.add(ReplyResponse.of(r, (replyLikeResult.contains(r.id!!))))
                commentsMap[nullableCommentId] = commentResponse
            }
        }

        return commentsMap.map { it.value }.toList()
    }

    private fun findAllByReplyIdsAndMemberUsername(replyIds: Set<Long>, username: String): MutableList<Long> {
        val replyLike = QReplyLike.replyLike
        return queryFactory
            .select(replyLike.reply.id).from(replyLike)
            .where(replyLike.reply.id.`in`(replyIds).and(replyLike.member.username.eq(username)))
            .fetch()
    }

    private fun findAllByCommentIdsAndMemberUsername(commentIds: Set<Long>, username: String): MutableList<Long> {
        val commentLike = QCommentLike.commentLike
        return queryFactory
            .select(commentLike.comment.id).from(commentLike)
            .where(commentLike.comment.id.`in`(commentIds).and(commentLike.member.username.eq(username)))
            .fetch()
    }

    private fun findAllByMemoIdWithPagination(memoId: Long, request: CommonPageRequest): MutableList<Tuple> {
        val comment = QComment.comment
        val reply = QReply.reply
        return queryFactory
            .select(comment, reply).from(comment).leftJoin(reply).on(comment.eq(reply.comment))
            .fetchJoin()
            .where(comment.memo.id.eq(memoId))
            .limit(request.size)
            .offset(request.size * request.page)
            .fetch()
    }
}