package app.kingmojang.domain.highlight.dto.response

import app.kingmojang.domain.highlight.domain.Highlight

data class HighlightResponse(
    val highlightId: Long,
    val content: String,
    val startNode: String,
    val startOffset: Int,
    val endNode: String,
    val endOffset: Int,
) {
    companion object {
        fun of(highlight: Highlight): HighlightResponse {
            return HighlightResponse(
                highlightId = highlight.id!!,
                content = highlight.content,
                startNode = highlight.startNode,
                startOffset = highlight.startOffset,
                endNode = highlight.endNode,
                endOffset = highlight.endOffset
            )
        }
    }
}