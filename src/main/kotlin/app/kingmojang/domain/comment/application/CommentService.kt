package app.kingmojang.domain.comment.application

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.exception.NotFoundCommentException
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.highlight.domain.Highlight
import app.kingmojang.domain.highlight.repository.HighlightRepository
import app.kingmojang.domain.like.domain.CommentLike
import app.kingmojang.domain.like.exception.NotFoundCommentLikeException
import app.kingmojang.domain.like.repository.CommentLikeRepository
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val highlightRepository: HighlightRepository
) {

    fun createComment(memoId: Long, memberId: Long, request: CommentRequest): Long {
        val writer = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        val highlight = request.highlight?.let { highlightRepository.save(Highlight.create(it)) }
        return commentRepository.save(Comment.create(writer, memo, highlight, request)).id!!
    }

    fun updateComment(commentId: Long, memberId: Long, request: CommentRequest): Long {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return comment.update(memberId, request).id!!
    }

    fun deleteComment(commentId: Long, memberId: Long) {
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        commentLikeRepository.deleteAllByIdInBatch(commentLikeRepository.findAllByCommentId(commentId))
        comment.remove(memberId)
    }

    @Transactional(readOnly = true)
    fun readComments(memoId: Long, memberId: Long?, request: PageRequest): CommentsResponse {
        val comments = commentRepository.findAllWithWriterByMemoIdAndDeletedFalse(memoId, request)
        val commentLikes = if (memberId != null) extractCommentsLike(comments, memberId) else emptyMap()
        return CommentsResponse.of(comments, commentLikes)
    }

    fun increaseCommentLikeCount(commentId: Long, memberId: Long): Long {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val comment = commentRepository.findByIdOrNull(commentId) ?: throw NotFoundCommentException(commentId)
        return commentLikeRepository.save(CommentLike.create(member, comment)).id!!
    }

    fun decreaseCommentLikeCount(commentId: Long, memberId: Long) {
        val commentLike = commentLikeRepository.findByCommentIdAndMemberId(commentId, memberId)
            ?: throw NotFoundCommentLikeException(commentId, memberId)
        commentLike.remove()
        commentLikeRepository.delete(commentLike)
    }

    @Transactional(readOnly = true)
    fun readCommentsByMember(memberId: Long, request: PageRequest): CommentsResponse {
        val comments = commentRepository.findAllWithWriterByWriterIdAndDeletedFalse(memberId, request)
        return CommentsResponse.of(comments, emptyMap())
    }

    private fun extractCommentsLike(comments: Slice<Comment>, memberId: Long): Map<Long, CommentLike> {
        val commentIds = comments.content.map { it.id!! }
        return commentLikeRepository.findAllByCommentIdInAndMemberId(commentIds, memberId)
            .associateBy { it.id!! }
    }
}