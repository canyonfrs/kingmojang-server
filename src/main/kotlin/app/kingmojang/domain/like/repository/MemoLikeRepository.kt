package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.MemoLike
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface MemoLikeRepository : JpaRepository<MemoLike, Long> {
    @EntityGraph(attributePaths = ["memo"])
    fun findByMemoIdAndMemberId(memoId: Long, memberId: Long): MemoLike?

    fun existsByMemoIdAndMemberId(memoId: Long, memberId: Long): Boolean

    fun findAllByMemoId(memoId: Long): List<MemoLike>

    @EntityGraph(attributePaths = ["member"])
    fun findAllByMemoIdInAndMemberId(memoIds: List<Long>, memberId: Long): List<MemoLike>
}