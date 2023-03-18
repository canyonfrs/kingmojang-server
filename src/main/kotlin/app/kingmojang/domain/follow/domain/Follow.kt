package app.kingmojang.domain.follow.domain

import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Follow(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    val creator: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    val follower: Member,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,
) {
    companion object {
        fun create(creator: Member, follower: Member): Follow {
            creator.increaseFollowerCount()
            return Follow(
                creator = creator,
                follower = follower,
                createdAt = LocalDateTime.now()
            )
        }
    }

    fun remove() {
        creator.decreaseFollowerCount()
    }
}