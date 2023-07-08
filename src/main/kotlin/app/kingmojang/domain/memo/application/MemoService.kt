package app.kingmojang.domain.memo.application

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.like.domain.CommentLike
import app.kingmojang.domain.like.domain.MemoLike
import app.kingmojang.domain.like.exception.NotFoundMemoLikeException
import app.kingmojang.domain.like.repository.CommentLikeRepository
import app.kingmojang.domain.like.repository.MemoLikeRepository
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.domain.Memo
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoService(
    private val memberRepository: MemberRepository,
    private val memoRepository: MemoRepository,
    private val memoLikeRepository: MemoLikeRepository,
    private val commentRepository: CommentRepository,
    private val commentLikeRepository: CommentLikeRepository,
    private val followRepository: FollowRepository,
) {

    @Transactional
    fun createMemo(memberId: Long, request: MemoRequest): Long {
        val writer = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        return memoRepository.save(Memo.create(writer, request)).id!!
    }

    @Transactional
    fun updateMemo(memoId: Long, memberId: Long, request: MemoRequest): Long {
        val memo = memoRepository.findMemoWithWriterByIdAndDeletedFalse(memoId) ?: throw NotFoundMemoException(memoId)
        return memo.update(memberId, request).id!!
    }

    @Transactional(readOnly = true)
    fun readMemo(memoId: Long, memberId: Long?, request: PageRequest): MemoResponse {
        val memo = memoRepository.findMemoWithWriterByIdAndDeletedFalse(memoId) ?: throw NotFoundMemoException(memoId)
        val memoLike = if (memberId != null) memoLikeRepository.existsByMemoIdAndMemberId(memoId, memberId) else false
        val isFollow =
            if (memberId != null) followRepository.existsByCreatorIdAndFollowerId(memo.writer.id!!, memberId) else false
        val comments = commentRepository.findAllWithWriterByMemoIdAndMemoDeletedFalseAndDeletedFalse(memoId, request)
        val commentLikes = if (memberId != null) extractCommentsLike(comments, memberId) else emptyMap()
        return MemoResponse.of(memo, memoLike, isFollow, CommentsResponse.of(comments, commentLikes))
    }

    @Transactional
    fun deleteMemo(memoId: Long, memberId: Long) {
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        memo.remove(memberId)
        memoLikeRepository.deleteAllInBatch(memoLikeRepository.findAllByMemoId(memoId))
    }

    @Transactional(readOnly = true)
    fun readMemosWrittenByCreator(writerId: Long, memberId: Long?, request: PageRequest): MemosResponse {
        val memos = memoRepository.findAllWithWriterByWriterIdAndDeletedFalse(writerId, request.withSort(updatedAtDesc()))
        val memoLikes = if (memberId != null) extractMemoLikes(memos, memberId) else emptyMap()
        return MemosResponse.of(memos, memoLikes)
    }

    @Transactional(readOnly = true)
    fun readMemosInUpdatedOrder(memberId: Long?, request: PageRequest): MemosResponse {
        val memos = memoRepository.findAllWithWriterByDeletedFalse(request.withSort(updatedAtDesc()))
        val memoLikes = if (memberId != null) extractMemoLikes(memos, memberId) else emptyMap()
        return MemosResponse.of(memos, memoLikes)
    }

    @Transactional(readOnly = true)
    fun readMemosInSubscribe(memberId: Long, request: PageRequest): MemosResponse {
        val creatorIds = followRepository.findAllByFollowerId(memberId, Sort.by("createdAt").descending())
            .map { it.creator.id!! }
        val memos = memoRepository.findAllWithWriterByWriterIdInAndDeletedFalse(creatorIds, request.withSort(updatedAtDesc()))
        val memoLikes = extractMemoLikes(memos, memberId)
        return MemosResponse.of(memos, memoLikes)
    }

    private fun extractMemoLikes(memos: Slice<Memo>, memberId: Long): Map<Long, MemoLike> {
        val memoIds = memos.content.map { it.id!! }
        return memoLikeRepository.findAllByMemoIdInAndMemberId(memoIds, memberId)
            .associateBy { it.memo.id!! }
    }

    private fun extractCommentsLike(comments: Slice<Comment>, memberId: Long): Map<Long, CommentLike> {
        val commentIds = comments.content.map { it.id!! }
        return commentLikeRepository.findAllByCommentIdInAndMemberId(commentIds, memberId)
            .associateBy { it.id!! }
    }


    @Transactional
    fun increaseMemoLikeCount(memoId: Long, memberId: Long): Long {
        val memo = memoRepository.findMemoWithWriterByIdAndDeletedFalse(memoId) ?: throw NotFoundMemoException(memoId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        return memoLikeRepository.save(MemoLike.create(member, memo)).id!!
    }

    @Transactional
    fun decreaseMemoLikeCount(memoId: Long, memberId: Long) {
        val memoLike = memoLikeRepository.findByMemoIdAndMemberId(memoId, memberId)
            ?: throw NotFoundMemoLikeException(memoId, memberId)
        memoLike.remove()
        return memoLikeRepository.delete(memoLike)
    }

    private fun updatedAtDesc(): Sort {
        return Sort.by(Sort.Direction.DESC, "updatedAt")
    }
}