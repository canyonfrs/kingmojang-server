package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Reply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long> {
    @EntityGraph(attributePaths = ["member"])
    fun findAllWithWriterByCommentIdAndDeletedFalse(commendId: Long, pageable: Pageable): Page<Reply>

    @EntityGraph(attributePaths = ["member"])
    fun findAllWithWriterByWriterIdAndDeletedFalse(memberId: Long, pageable: Pageable): Page<Reply>
}