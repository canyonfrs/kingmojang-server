package app.kingmojang.domain.memo.application

import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.like.domain.MemoLike
import app.kingmojang.domain.like.exception.NotFoundMemoLikeException
import app.kingmojang.domain.like.repository.MemoLikeRepository
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
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
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
    fun createMemo(userDetails: UserDetails, request: MemoRequest): Long {
        validate(userDetails.username, request.username)
        val writer = memberRepository.findByUsername(userDetails.username)
            ?: throw NotFoundUsernameException(userDetails.username)
        return memoRepository.save(Memo.create(writer, request)).id!!
    }

    @Transactional
    fun updateMemo(userDetails: UserDetails, id: Long, request: MemoRequest): Long {
        validate(userDetails.username, request.username)
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
    fun readMemoWithUsername(userDetails: UserDetails, id: Long, request: CommonPageRequest): MemoResponse {
        val username = userDetails.username
        val memo = memoQueryRepository.findByIdOrNullWithUsername(id, username)
            ?: throw NotFoundMemoException(id)
        val commentsResponse = CommentsResponse.of(
            commentQueryRepository.readCommentsInMemoWithUsername(id, username, request)
        )
        return MemoResponse.of(memo, commentsResponse)
    }

    @Transactional
    fun deleteMemo(userDetails: UserDetails, id: Long) {
        val memo = memoRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        validate(userDetails.username, memo.writer.username)
        memoLikeRepository.deleteAllByIdInBatch(memoLikeRepository.findAllByMemoId(id))
        memoRepository.delete(memo)
    }

    @Transactional(readOnly = true)
    fun readMemosWrittenByMember(username: String, request: NoOffsetPageRequest): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosWrittenByMember(request, username))
    }

    @Transactional(readOnly = true)
    fun readMemosInUpdatedOrder(size: Int): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosInUpdatedOrder(size))
    }

    @Transactional
    fun increaseMemoLikeCount(userDetails: UserDetails, id: Long, username: String): Long {
        validate(userDetails.username, username)
        val memo = memoRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        val member = memberRepository.findByUsername(username) ?: throw NotFoundUsernameException(username)
        return memoLikeRepository.save(MemoLike.create(member, memo)).id!!
    }

    @Transactional
    fun decreaseMemoLikeCount(userDetails: UserDetails, id: Long, username: String) {
        validate(userDetails.username, username)
        val memoLike = memoLikeRepository.findByMemoIdAndMemberUsername(id, username)
            ?: throw NotFoundMemoLikeException(id, username)
        memoLike.remove()
        return memoLikeRepository.delete(memoLike)
    }

    private fun validate(tokenUsername: String, reqUsername: String) {
        if (tokenUsername != reqUsername) {
            throw CommonException(INVALID_JWT_TOKEN)
        }
    }
}
