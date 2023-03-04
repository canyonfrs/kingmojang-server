package app.kingmojang.domain.memo.domain

import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Memo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memo_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val writer: Member,

    var title: String,
    @Lob
    var content: String,

    var likeCount: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
) {
}