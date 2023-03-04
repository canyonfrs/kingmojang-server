package app.kingmojang.domain.comment.domain

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
    val writerId: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    val memoId: Memo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    val parent : Comment?,

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    @Column(name = "child_comments")
    val childComments: MutableList<Comment> = mutableListOf(),

    @Lob
    var content: String,

    var likeCount: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
) {
}