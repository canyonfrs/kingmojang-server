package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.MemoLike
import org.springframework.data.jpa.repository.JpaRepository

interface MemoLikeRepository: JpaRepository<MemoLike, Long> {
    fun findByMemoIdAndMemberUsername(memoId: Long, username: String): MemoLike?

    fun findAllByMemoId(memoId: Long): List<Long>
}