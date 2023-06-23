package app.kingmojang.domain.highlight.dto.request

data class HighlightRequest(
    val content: String,
    val startNode: String,
    val startOffset: Int,
    val endNode: String,
    val endOffset: Int,
) {
}
