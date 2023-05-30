package app.kingmojang.domain.highlight.domain

import jakarta.persistence.*

@Entity
class Highlight(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "highlight_id")
    val id: Long? = null,

    @Lob
    @Column(columnDefinition = "mediumtext")
    val content: String,

    @Column(name = "start_x")
    val startX: Int,

    @Column(name = "start_y")
    val startY: Int,

    @Column(name = "end_x")
    val endX: Int,

    @Column(name = "end_y")
    val endY: Int,

    val version: Int,
) {
}