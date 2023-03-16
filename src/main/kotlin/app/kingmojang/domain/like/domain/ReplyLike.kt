package app.kingmojang.domain.like.domain

import app.kingmojang.domain.comment.domain.Reply
import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class ReplyLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_like_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    val reply: Reply,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(member: Member, reply: Reply): ReplyLike {
            return ReplyLike(
                member = member,
                reply = reply,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun remove() {
        this.reply.decreaseLikeCount()
    }
}