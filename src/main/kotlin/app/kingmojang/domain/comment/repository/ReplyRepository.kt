package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Reply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long> {
    @EntityGraph(attributePaths = ["writer"])
    fun findReplyWithWriterByIdAndDeletedFalse(replyId: Long): Reply?

    @EntityGraph(attributePaths = ["writer"])
    fun findAllWithWriterByCommentIdAndDeletedFalse(commendId: Long, pageable: Pageable): Page<Reply>

    @EntityGraph(attributePaths = ["writer"])
    fun findAllWithWriterByWriterIdAndDeletedFalse(memberId: Long, pageable: Pageable): Page<Reply>
}