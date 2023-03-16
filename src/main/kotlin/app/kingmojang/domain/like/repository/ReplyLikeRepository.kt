package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.ReplyLike
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyLikeRepository : JpaRepository<ReplyLike, Long> {
    fun findByReplyIdAndMemberUsername(replyId: Long, username: String): ReplyLike?

    fun findAllByReplyId(replyId: Long): List<Long>
}