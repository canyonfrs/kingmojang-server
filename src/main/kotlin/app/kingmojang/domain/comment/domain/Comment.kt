package app.kingmojang.domain.comment.domain

import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Memo
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val writer: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    val memo: Memo,

    @Lob
    var content: String,

    var likeCount: Int,

    var replyCount: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
) {
    companion object {
        fun create(writer: Member, memo: Memo, request: CommentRequest): Comment {
            val now = LocalDateTime.now()
            memo.increaseCommentCount()
            return Comment(
                writer = writer,
                memo = memo,
                content = request.content,
                likeCount = 0,
                replyCount = 0,
                createdAt = now,
                updatedAt = now
            )
        }
    }

    fun update(request: CommentRequest): Comment {
        this.content = request.content
        this.updatedAt = LocalDateTime.now()
        return this
    }

    fun increaseReplyCount() = this.replyCount++

    fun decreaseReplyCount() = this.replyCount--

    fun increaseLikeCount() = this.likeCount++

    fun decreaseLikeCount() = this.likeCount--

    fun remove() {
        this.memo.decreaseCommentCount()
    }
}