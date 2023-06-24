package app.kingmojang.fixture

import app.kingmojang.domain.highlight.domain.Highlight
import app.kingmojang.domain.highlight.dto.request.HighlightRequest
import app.kingmojang.domain.highlight.dto.response.HighlightResponse


const val HIGHLIGHT_ID = 1L
const val HIGHLIGHT_CONTENT = "하이라이트 내용"
const val START_NODE = "startNodeName"
const val START_OFFSET = 0
const val END_NODE = "endNodeName"
const val END_OFFSET = 1234

fun createHighlight(
    highlightId: Long = HIGHLIGHT_ID,
    content: String = HIGHLIGHT_CONTENT,
    startNode: String = START_NODE,
    startOffset: Int = START_OFFSET,
    endNode: String = END_NODE,
    endOffset: Int = END_OFFSET
): Highlight {
    return Highlight(highlightId, content, startNode, startOffset, endNode, endOffset)
}

fun createHighlightRequest(
    content: String = HIGHLIGHT_CONTENT,
    startNode: String = START_NODE,
    startOffset: Int = START_OFFSET,
    endNode: String = END_NODE,
    endOffset: Int = END_OFFSET
): HighlightRequest {
    return HighlightRequest(content, startNode, startOffset, endNode, endOffset)
}

fun createHighlightResponse(
    highlightId: Long = HIGHLIGHT_ID,
    content: String = HIGHLIGHT_CONTENT,
    startNode: String = START_NODE,
    startOffset: Int = START_OFFSET,
    endNode: String = END_NODE,
    endOffset: Int = END_OFFSET
): HighlightResponse {
    return HighlightResponse(highlightId, content, startNode, startOffset, endNode, endOffset)
}