package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Comment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = ["member"])
    fun findAllWithWriterByMemoIdAndDeletedFalse(memoId: Long, pageable: Pageable): Page<Comment>

    @EntityGraph(attributePaths = ["member"])
    fun findAllWithWriterByWriterIdAndDeletedFalse(memberId: Long, pageable: Pageable): Page<Comment>
}