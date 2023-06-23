package app.kingmojang.domain.comment.domain

import app.kingmojang.domain.SoftDeletable
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Memo
import app.kingmojang.global.exception.common.NotWriterException
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Reply(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val writer: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    val comment: Comment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    val memo: Memo,

    @Lob
    @Column(columnDefinition = "longtext")
    var content: String,

    var likeCount: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
) : SoftDeletable() {
    companion object {
        fun create(writer: Member, memo: Memo, comment: Comment, request: ReplyRequest): Reply {
            val now = LocalDateTime.now()
            comment.increaseReplyCount()
            return Reply(
                writer = writer,
                comment = comment,
                memo = memo,
                content = request.content,
                likeCount = 0,
                createdAt = now,
                updatedAt = now,
            )
        }
    }

    fun increaseLikeCount() = this.likeCount++

    fun decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--
        }
    }

    fun update(memberId: Long, request: ReplyRequest): Reply {
        if (!isWriter(memberId)) {
            throw NotWriterException(memberId)
        }
        this.content = request.content
        this.updatedAt = LocalDateTime.now()
        return this
    }

    fun remove(memberId: Long) {
        if (!isWriter(memberId)) {
            throw NotWriterException(memberId)
        }
        this.changeToDelete()
        this.comment.decreaseReplyCount()
    }

    private fun isWriter(memberId: Long) = this.writer.id == memberId
}