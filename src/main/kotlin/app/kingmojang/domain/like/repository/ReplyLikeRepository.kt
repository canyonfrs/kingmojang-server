package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.ReplyLike
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyLikeRepository : JpaRepository<ReplyLike, Long> {
    @EntityGraph(attributePaths = ["reply"])
    fun findByReplyIdAndMemberId(replyId: Long, memberId: Long): ReplyLike?

    fun findAllByReplyId(replyId: Long): List<Long>

    @EntityGraph(attributePaths = ["member"])
    fun findAllByReplyIdInAndMemberId(replyIds: List<Long>, memberId: Long): List<ReplyLike>
}