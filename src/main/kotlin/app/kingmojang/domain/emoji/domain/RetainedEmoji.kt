package app.kingmojang.domain.emoji.domain

import app.kingmojang.domain.member.domain.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class RetainedEmoji(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "retained_emoji_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id")
    val emoji: Emoji,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,


    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    val emojiStatus: EmojiStatus,

    val howToGet: String,
) {
}