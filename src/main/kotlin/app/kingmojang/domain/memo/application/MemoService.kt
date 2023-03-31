package app.kingmojang.domain.memo.application

import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.like.domain.MemoLike
import app.kingmojang.domain.like.exception.NotFoundMemoLikeException
import app.kingmojang.domain.like.repository.MemoLikeRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.exception.NotFoundUsernameException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.domain.Memo
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoQueryRepository
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.common.request.NoOffsetPageRequest
import app.kingmojang.global.validator.MemberIdValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoService(
    private val memoRepository: MemoRepository,
    private val memoQueryRepository: MemoQueryRepository,
    private val commentQueryRepository: CommentQueryRepository,
    private val memberRepository: MemberRepository,
    private val memoLikeRepository: MemoLikeRepository,
) {

    @Transactional
    fun createMemo(userPrincipal: UserPrincipal, request: MemoRequest): Long {
        MemberIdValidator.validate(userPrincipal.getId(), request.memberId)
        val writer = memberRepository.findByUsername(userPrincipal.username)
            ?: throw NotFoundUsernameException(userPrincipal.username)
        return memoRepository.save(Memo.create(writer, request)).id!!
    }

    @Transactional
    fun updateMemo(userPrincipal: UserPrincipal, id: Long, request: MemoRequest): Long {
        MemberIdValidator.validate(userPrincipal.getId(), request.memberId)
        val memo = memoRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        return memo.update(request).id!!
    }

    @Transactional(readOnly = true)
    fun readMemo(id: Long, request: CommonPageRequest): MemoResponse {
        val memo = memoQueryRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        val commentsResponse = CommentsResponse.of(commentQueryRepository.readCommentsInMemo(id, request))
        return MemoResponse.of(memo, commentsResponse)
    }

    @Transactional(readOnly = true)
    fun readMemoWithUsername(userPrincipal: UserPrincipal, id: Long, request: CommonPageRequest): MemoResponse {
        val username = userPrincipal.username
        val memo = memoQueryRepository.findByIdOrNullWithUsername(id, username)
            ?: throw NotFoundMemoException(id)
        val commentsResponse = CommentsResponse.of(
            commentQueryRepository.readCommentsInMemoWithUsername(id, username, request)
        )
        return MemoResponse.of(memo, commentsResponse)
    }

    @Transactional
    fun deleteMemo(userPrincipal: UserPrincipal, id: Long) {
        val memo = memoRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        MemberIdValidator.validate(userPrincipal.getId(), memo.writer.id!!)
        memoLikeRepository.deleteAllByIdInBatch(memoLikeRepository.findAllByMemoId(id))
        memoRepository.delete(memo)
    }

    @Transactional(readOnly = true)
    fun readMemosWrittenByMember(memberId: Long, request: NoOffsetPageRequest): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosWrittenByMember(request, memberId))
    }

    @Transactional(readOnly = true)
    fun readMemosInUpdatedOrder(size: Int): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosInUpdatedOrder(size))
    }

    @Transactional
    fun increaseMemoLikeCount(userPrincipal: UserPrincipal, memoId: Long, memberId: Long): Long {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val memo = memoRepository.findByIdOrNull(memoId) ?: throw NotFoundMemoException(memoId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        return memoLikeRepository.save(MemoLike.create(member, memo)).id!!
    }

    @Transactional
    fun decreaseMemoLikeCount(userPrincipal: UserPrincipal, memoId: Long, memberId: Long) {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val memoLike = memoLikeRepository.findByMemoIdAndMemberId(memoId, memberId)
            ?: throw NotFoundMemoLikeException(memoId, memberId)
        memoLike.remove()
        return memoLikeRepository.delete(memoLike)
    }
}
