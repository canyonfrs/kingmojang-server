package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = ["writer"])
    fun findCommentWithWriterByIdAndDeletedFalse(commentId: Long): Comment?

    @EntityGraph(attributePaths = ["writer", "highlight"])
    fun findAllWithWriterByMemoIdAndMemoDeletedFalseAndDeletedFalse(memoId: Long, pageable: Pageable): Page<Comment>

    @EntityGraph(attributePaths = ["writer", "highlight"])
    fun findAllWithWriterByWriterIdAndDeletedFalse(memberId: Long, pageable: Pageable): Page<Comment>
}