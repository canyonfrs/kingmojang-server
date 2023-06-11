package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.like.domain.CommentLike
import app.kingmojang.domain.like.exception.NotFoundCommentLikeException
import app.kingmojang.domain.like.repository.CommentLikeRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.validator.MemberIdValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val commentQueryRepository: CommentQueryRepository,
    private val commentLikeRepository: CommentLikeRepository,
) {

    @Transactional
    fun createComment(userPrincipal: UserPrincipal, memoId: Long, request: CommentRequest): Long {
        val memberId = request.memberId
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val writer = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        return commentRepository.save(Comment.create(writer, memo, request)).id!!
    }

    @Transactional
    fun updateComment(userPrincipal: UserPrincipal, commentId: Long, request: CommentRequest): Long {
        MemberIdValidator.validate(userPrincipal.getId(), request.memberId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return comment.update(request).id!!
    }

    @Transactional
    fun deleteComment(userPrincipal: UserPrincipal, commentId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        MemberIdValidator.validate(userPrincipal.getId(), comment.writer.id!!)
        commentLikeRepository.deleteAllByIdInBatch(commentLikeRepository.findAllByCommentId(commentId))
        comment.remove()
    }

    @Transactional(readOnly = true)
    fun readComments(memoId: Long, request: CommonPageRequest): CommentsResponse {
        return CommentsResponse.of(commentQueryRepository.readCommentsInMemo(memoId, request))
    }

    @Transactional
    fun increaseCommentLikeCount(userPrincipal: UserPrincipal, id: Long, memberId: Long): Long {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val comment = commentRepository.findByIdOrNull(id) ?: throw NotFoundCommentException(id)
        return commentLikeRepository.save(CommentLike.create(member, comment)).id!!
    }

    @Transactional
    fun decreaseCommentLikeCount(userPrincipal: UserPrincipal, id: Long, memberId: Long) {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val commentLike = commentLikeRepository.findByCommentIdAndMemberId(id, memberId)
            ?: throw NotFoundCommentLikeException(id, memberId)
        commentLike.remove()
        commentLikeRepository.delete(commentLike)
    }

    @Transactional(readOnly = true)
    fun readCommentsByMember(memberId: Long, request: CommonPageRequest): CommentsResponse {
        return CommentsResponse.of(commentQueryRepository.readCommentsByMember(memberId, request))
    }
}