package app.kingmojang.domain.comment.repository

import app.kingmojang.domain.comment.domain.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}