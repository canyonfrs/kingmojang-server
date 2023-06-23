package app.kingmojang.domain.highlight.domain

import app.kingmojang.domain.highlight.dto.request.HighlightRequest
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

    @Column(name = "start_node")
    val startNode: String,

    @Column(name = "start_offset")
    val startOffset: Int,

    @Column(name = "end_node")
    val endNode: String,

    @Column(name = "end_offset")
    val endOffset: Int,
) {
    companion object {
        fun create(request: HighlightRequest): Highlight {
            return Highlight(
                content = request.content,
                startNode = request.startNode,
                startOffset = request.startOffset,
                endNode = request.endNode,
                endOffset = request.endOffset
            )
        }
    }
}