package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.MemoLike
import org.springframework.data.jpa.repository.JpaRepository

interface MemoLikeRepository: JpaRepository<MemoLike, Long> {
    fun findByMemoIdAndMemberId(memoId: Long, memberId: Long): MemoLike?

    fun findAllByMemoId(memoId: Long): List<Long>
}