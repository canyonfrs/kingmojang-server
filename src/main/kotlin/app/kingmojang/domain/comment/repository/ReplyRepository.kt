package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Reply
import org.springframework.data.jpa.repository.JpaRepository

interface ReplyRepository : JpaRepository<Reply, Long> {
}