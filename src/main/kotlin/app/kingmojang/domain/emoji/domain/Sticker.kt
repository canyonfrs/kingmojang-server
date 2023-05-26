package app.kingmojang.domain.emoji.domain

import jakarta.persistence.*

@Entity
class Sticker(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sticker_id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoji_id")
    val emoji: Emoji,

    var address: String
) {

}