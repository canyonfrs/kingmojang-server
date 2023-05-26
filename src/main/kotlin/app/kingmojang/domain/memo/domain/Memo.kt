package app.kingmojang.domain.memo.domain

import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.dto.request.MemoRequest
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

    var commentCount: Int,

    @Embedded
    var font: Font,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,

    var version: Int = 0,
) {
    companion object {
        fun create(writer: Member, request: MemoRequest): Memo {
            val now = LocalDateTime.now()
            return Memo(
                writer = writer,
                title = request.title,
                content = request.content,
                font = Font.of(request),
                likeCount = 0,
                commentCount = 0,
                createdAt = now,
                updatedAt = now,
            )
        }
    }

    fun update(request: MemoRequest): Memo {
        this.title = request.title
        this.content = request.content
        this.font = Font.of(request)
        this.updatedAt = LocalDateTime.now()
        return this
    }

    fun increaseCommentCount() {
        this.commentCount++
    }

    fun decreaseCommentCount() {
        this.commentCount--
    }

    fun increaseLikeCount() {
        this.likeCount++
    }

    fun decreaseLikeCount() {
        this.likeCount--
    }
}

@Embeddable
data class Font(
    @Column(name = "font_name")
    val name: String,

    @Column(name = "font_style")
    val style: String,

    @Column(name = "font_size")
    val size: Int,
) {
    companion object {
        fun of(request: MemoRequest): Font {
            return Font(
                name = request.fontName,
                style = request.fontStyle,
                size = request.fontSize,
            )
        }
    }
}