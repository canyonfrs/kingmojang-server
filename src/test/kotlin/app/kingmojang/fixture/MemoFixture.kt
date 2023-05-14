package app.kingmojang.fixture

import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoNestedResponse
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import java.time.LocalDateTime

const val MEMO_ID = 1L
const val TITLE = "오늘은 무엇을 할까?"
const val CONTENT = "안녕하세요. 오늘은 무슨일을 할까요? 무엇을 해야할까요?"
const val SUMMARY = "안녕하세요. ..."
const val FONT_SIZE = 12
const val FONT_NAME = "JetBrains Mono"
const val FONT_STYLE = "Bold"
const val MEMO_IS_LIKE = false
const val MEMO_LIKE_COUNT = 0
const val MEMO_COMMENT_COUNT = 3
const val MEMO_LIKE_ID = 1L
val MEMO_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val MEMO_UPDATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 42, 52, 128)

fun createMemoRequest(
    title: String = TITLE,
    memberId: Long = MEMBER_ID,
    content: String = CONTENT,
    fontName: String = FONT_NAME,
    fontStyle: String = FONT_STYLE,
    fontSize: Int = FONT_SIZE
): MemoRequest {
    return MemoRequest(
        title, memberId, content, fontName, fontStyle, fontSize
    )
}

fun createMemoResponse(
    memoId: Long = MEMO_ID,
    title: String = TITLE,
    writer: String = NICKNAME,
    content: String = CONTENT,
    likeCount: Int = MEMO_LIKE_COUNT,
    commentCount: Int = MEMO_COMMENT_COUNT,
    isLike: Boolean = MEMO_IS_LIKE,
    createdAt: LocalDateTime = MEMO_CREATED_AT,
    updatedAt: LocalDateTime = MEMO_UPDATED_AT,
    fontName: String = FONT_NAME,
    fontStyle: String = FONT_STYLE,
    fontSize: Int = FONT_SIZE,
): MemoResponse {
    return MemoResponse(
        memoId,
        title,
        writer,
        content,
        likeCount,
        commentCount,
        isLike,
        createdAt,
        updatedAt,
        fontName,
        fontStyle,
        fontSize,
        createCommentsResponse()
    )
}

fun createMemosResponse(
    nestedResponses: List<MemoNestedResponse> = listOf(
        createMemoNestedResponse(),
        createMemoNestedResponse(),
        createMemoNestedResponse(),
        createMemoNestedResponse(),
        createMemoNestedResponse(),
    )
): MemosResponse {
    return MemosResponse(nestedResponses)
}

fun createMemoNestedResponse(
    memoId: Long = MEMO_ID,
    title: String = TITLE,
    writer: String = NICKNAME,
    summary: String = SUMMARY,
    likeCount: Int = MEMO_LIKE_COUNT,
    commentCount: Int = MEMO_COMMENT_COUNT,
    createdAt: LocalDateTime = MEMO_CREATED_AT,
    updatedAt: LocalDateTime = MEMO_UPDATED_AT,
    fontName: String = FONT_NAME,
    fontStyle: String = FONT_STYLE,
    fontSize: Int = FONT_SIZE,
): MemoNestedResponse {
    return MemoNestedResponse(
        memoId, title, writer, summary, likeCount, commentCount, createdAt, updatedAt, fontName, fontStyle, fontSize
    )
}