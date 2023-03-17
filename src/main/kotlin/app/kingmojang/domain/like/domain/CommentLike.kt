package app.kingmojang.domain.like.domain

import app.kingmojang.domain.comment.domain.Comment
import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class CommentLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    val comment: Comment,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(member: Member, comment: Comment): CommentLike {
            comment.increaseLikeCount()
            return CommentLike(
                member = member,
                comment = comment,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun remove() {
        this.comment.decreaseLikeCount()
    }
}