package app.kingmojang.domain.like.repository

import app.kingmojang.domain.like.domain.CommentLike
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CommentLikeRepository : JpaRepository<CommentLike, Long> {
    @EntityGraph(attributePaths = ["comment"])
    fun findByCommentIdAndMemberId(commentId: Long, memberId: Long): CommentLike?

    fun findAllByCommentId(commentId: Long): List<CommentLike>

    @EntityGraph(attributePaths = ["member"])
    fun findAllByCommentIdInAndMemberId(commentIds: List<Long>, memberId: Long): List<CommentLike>
}