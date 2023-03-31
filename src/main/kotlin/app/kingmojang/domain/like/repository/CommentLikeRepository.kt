package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.CommentLike
import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
    fun findByCommentIdAndMemberId(commentId: Long, memberId: Long): CommentLike?

    fun findAllByCommentId(commentId: Long): List<Long>
}