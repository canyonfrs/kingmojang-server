package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.exception.NotFoundReplyException
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.comment.repository.ReplyRepository
import app.kingmojang.domain.member.exception.NotFoundUsernameException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ReplyService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val replyRepository: ReplyRepository,
) {
    @Transactional
    fun createReply(userDetails: UserDetails, memoId: Long, commentId: Long, request: ReplyRequest): Long {
        val username = userDetails.username
        validate(username, request.username)
        val writer = memberRepository.findByUsername(username) ?: throw NotFoundUsernameException(username)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return replyRepository.save(Reply.create(writer, memo, comment, request)).id!!
    }

    @Transactional
    fun updateReply(userDetails: UserDetails, replyId: Long, request: ReplyRequest): Long {
        val username = userDetails.username
        validate(username, request.username)
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        return reply.update(request).id!!
    }

    @Transactional
    fun deleteReply(userDetails: UserDetails, replyId: Long) {
        val reply = replyRepository.findByIdOrNull(replyId) ?: throw NotFoundReplyException(replyId)
        validate(userDetails.username, reply.writer.username)
        reply.remove()
        replyRepository.delete(reply)
    }

    private fun validate(tokenUsername: String, reqUsername: String) {
        if (tokenUsername != reqUsername) {
            throw CommonException(ErrorCodes.INVALID_JWT_TOKEN)
        }
    }
}