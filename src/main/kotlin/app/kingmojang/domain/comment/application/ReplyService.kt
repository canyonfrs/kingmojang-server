package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.dto.response.RepliesResponse
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.exception.NotFoundReplyException
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.comment.repository.ReplyRepository
import app.kingmojang.domain.like.domain.ReplyLike
import app.kingmojang.domain.like.exception.NotFoundReplyLikeException
import app.kingmojang.domain.like.repository.ReplyLikeRepository
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReplyService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
    private val replyLikeRepository: ReplyLikeRepository,
) {

    fun createReply(memoId: Long, commentId: Long, memberId: Long, request: ReplyRequest): Long {
        val writer = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return replyRepository.save(Reply.create(writer, memo, comment, request)).id!!
    }

    fun updateReply(replyId: Long, memberId: Long, request: ReplyRequest): Long {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        return reply.update(memberId, request).id!!
    }

    fun deleteReply(replyId: Long, memberId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        reply.remove(memberId)
        replyLikeRepository.deleteAllByIdInBatch(replyLikeRepository.findAllByReplyId(replyId))
    }

    fun increaseReplyLikeCount(replyId: Long, memberId: Long): Long {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        return replyLikeRepository.save(ReplyLike.create(member, reply)).id!!
    }

    fun decreaseReplyLikeCount(replyId: Long, memberId: Long) {
        val reply = replyLikeRepository.findByReplyIdAndMemberId(replyId, memberId)
            ?: throw NotFoundReplyLikeException(replyId, memberId)
        reply.remove()
        replyLikeRepository.delete(reply)
    }

    @Transactional(readOnly = true)
    fun readReplies(commentId: Long, memberId: Long?, request: PageRequest): RepliesResponse {
        val replies = replyRepository.findAllWithWriterByCommentIdAndDeletedFalse(commentId, request)
        val replyIds = replies.content.map { it.id!! }
        val repliesLikes = if (memberId != null) replyLikeRepository.findAllByReplyIdInAndMemberId(replyIds, memberId)
            .associateBy { it.reply.id!! } else emptyMap()
        return RepliesResponse.of(replies, repliesLikes)
    }

    @Transactional(readOnly = true)
    fun readRepliesByMember(memberId: Long, request: PageRequest) : RepliesResponse {
        val replies = replyRepository.findAllWithWriterByWriterIdAndDeletedFalse(memberId, request)
        return RepliesResponse.of(replies, emptyMap())
    }
}