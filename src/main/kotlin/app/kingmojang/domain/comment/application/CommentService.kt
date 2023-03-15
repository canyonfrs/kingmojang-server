package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.member.exception.NotFoundUsernameException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val commentQueryRepository: CommentQueryRepository,
) {

    @Transactional
    fun createComment(userDetails: UserDetails, memoId: Long, request: CommentRequest): Long {
        val username = userDetails.username
        validate(username, request.username)
        val writer = memberRepository.findByUsername(username) ?: throw NotFoundUsernameException(username)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        return commentRepository.save(Comment.create(writer, memo, request)).id!!
    }

    @Transactional
    fun updateComment(userDetails: UserDetails, commentId: Long, request: CommentRequest): Long {
        val username = userDetails.username
        validate(username, request.username)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return comment.update(request).id!!
    }

    @Transactional
    fun deleteComment(userDetails: UserDetails, commentId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        validate(userDetails.username, comment.writer.username)
        comment.remove()
        commentRepository.delete(comment)
    }

    @Transactional(readOnly = true)
    fun readComments(memoId: Long, request: CommonPageRequest): CommentsResponse {
        return CommentsResponse.of(commentQueryRepository.readCommentsInMemo(memoId, request))
    }
    private fun validate(tokenUsername: String, reqUsername: String) {
        if (tokenUsername != reqUsername) {
            throw CommonException(ErrorCodes.INVALID_JWT_TOKEN)
        }
    }
}