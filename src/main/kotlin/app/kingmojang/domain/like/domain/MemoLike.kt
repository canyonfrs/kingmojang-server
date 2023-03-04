package app.kingmojang.domain.like.domain

import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Memo
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class MemoLike(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_like_Id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val memberId: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memo_id")
    val memoId: Memo,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
) {
}