package app.kingmojang.domain.memo.application

import app.kingmojang.domain.member.exception.NotFoundUsernameException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.domain.Memo
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import app.kingmojang.domain.memo.exception.NotFoundMemoException
import app.kingmojang.domain.memo.repository.MemoQueryRepository
import app.kingmojang.domain.memo.repository.MemoRepository
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
    private val memberRepository: MemberRepository,
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

    private fun validate(tokenUsername: String, reqUsername: String) {
        if (tokenUsername != reqUsername) {
            throw CommonException(INVALID_JWT_TOKEN)
        }
    }

    @Transactional(readOnly = true)
    fun readMemo(id: Long): MemoResponse {
        val memo = memoQueryRepository.findByIdOrNull(id) ?: throw NotFoundMemoException(id)
        return MemoResponse.of(memo)
    }

    @Transactional(readOnly = true)
    fun readMemosWrittenByMember(username: String, request: NoOffsetPageRequest): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosWrittenByMember(request, username))
    }

    @Transactional(readOnly = true)
    fun readMemosInUpdatedOrder(size: Int): MemosResponse {
        return MemosResponse.of(memoQueryRepository.findMemosInUpdatedOrder(size))
    }
}
