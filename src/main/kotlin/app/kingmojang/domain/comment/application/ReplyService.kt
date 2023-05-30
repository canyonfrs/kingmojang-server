package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.exception.NotFoundReplyException
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.comment.repository.ReplyRepository
import app.kingmojang.domain.like.domain.ReplyLike
import app.kingmojang.domain.like.exception.NotFoundReplyLikeException
import app.kingmojang.domain.like.repository.ReplyLikeRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.global.validator.MemberIdValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReplyService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
    private val replyLikeRepository: ReplyLikeRepository,
) {
    @Transactional
    fun createReply(userPrincipal: UserPrincipal, memoId: Long, commentId: Long, request: ReplyRequest): Long {
        val memberId = request.memberId
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val writer = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return replyRepository.save(Reply.create(writer, memo, comment, request)).id!!
    }

    @Transactional
    fun updateReply(userPrincipal: UserPrincipal, replyId: Long, request: ReplyRequest): Long {
        MemberIdValidator.validate(userPrincipal.getId(), request.memberId)
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        return reply.update(request).id!!
    }

    @Transactional
    fun deleteReply(userPrincipal: UserPrincipal, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        MemberIdValidator.validate(userPrincipal.getId(), reply.writer.id!!)
        reply.remove()
        replyLikeRepository.deleteAllByIdInBatch(replyLikeRepository.findAllByReplyId(replyId))
    }

    @Transactional
    fun increaseReplyLikeCount(userPrincipal: UserPrincipal, replyId: Long, memberId: Long): Long {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        return replyLikeRepository.save(ReplyLike.create(member, reply)).id!!
    }

    @Transactional
    fun decreaseReplyLikeCount(userPrincipal: UserPrincipal, replyId: Long, memberId: Long) {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val reply = replyLikeRepository.findByReplyIdAndMemberId(replyId, memberId)
            ?: throw NotFoundReplyLikeException(replyId, memberId)
        reply.remove()
        replyLikeRepository.delete(reply)
    }
}