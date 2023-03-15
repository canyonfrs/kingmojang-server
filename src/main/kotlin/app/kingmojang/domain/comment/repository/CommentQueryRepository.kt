package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.QComment
import app.kingmojang.domain.comment.domain.QReply
import app.kingmojang.domain.comment.dto.response.CommentResponse
import app.kingmojang.domain.comment.dto.response.ReplyResponse
import app.kingmojang.global.common.request.CommonPageRequest
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class CommentQueryRepository(
    private val queryFactory: JPAQueryFactory,
) {
    fun readCommentsInMemo(memoId: Long, request: CommonPageRequest): List<CommentResponse> {
        val comment = QComment.comment
        val reply = QReply.reply

        val result = queryFactory
            .select(comment, reply).from(comment).leftJoin(reply).on(comment.eq(reply.comment))
            .fetchJoin()
            .where(comment.memo.id.eq(memoId))
            .limit(request.size)
            .offset(request.size * request.page)
            .fetch()

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
}