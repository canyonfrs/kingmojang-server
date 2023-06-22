package app.kingmojang.fixture

import app.kingmojang.domain.like.domain.MemoLike
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.memo.domain.Font
import app.kingmojang.domain.memo.domain.Memo
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import java.time.LocalDateTime

const val MEMO_ID = 1L
const val TITLE = "오늘은 무엇을 할까?"
const val CONTENT = "안녕하세요. 오늘은 무슨일을 할까요? 무엇을 해야할까요?"
const val SUMMARY = "안녕하세요. ..."
const val FONT_SIZE = 12
const val FONT_NAME = "JetBrains Mono"
const val FONT_STYLE = "Bold"
const val MEMO_IS_LIKE = false
const val MEMO_IS_FOLLOW = false
const val MEMO_LIKE_COUNT = 0
const val MEMO_COMMENT_COUNT = 3
const val MEMO_LIKE_ID = 1L
val MEMO_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val MEMO_LIKE_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 30, 41, 311)
val MEMO_UPDATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 1, 12, 42, 52, 128)

fun createMemoRequest(
    title: String = TITLE,
    content: String = CONTENT,
    fontName: String = FONT_NAME,
    fontStyle: String = FONT_STYLE,
    fontSize: Int = FONT_SIZE
): MemoRequest {
    return MemoRequest(
        title, content, fontName, fontStyle, fontSize
    )
}

fun createMemoResponse(
    memoId: Long = MEMO_ID,
    title: String = TITLE,
    writer: String = NICKNAME,
    content: String = CONTENT,
    likeCount: Int = MEMO_LIKE_COUNT,
    commentCount: Int = MEMO_COMMENT_COUNT,
    isFollow: Boolean = MEMO_IS_FOLLOW,
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
        createdAt,
        updatedAt,
        fontName,
        fontStyle,
        fontSize,
        isFollow,
        isLike,
        createCommentsResponse()
    )
}

fun createMemosResponse(): MemosResponse {
    return MemosResponse.of(createMemoSlice(), emptyMap())
}

fun createMemo(
    memoId: Long = MEMO_ID,
    writer: Member = createMember(),
    title: String = TITLE,
    content: String = CONTENT,
    likeCount: Int = MEMO_LIKE_COUNT,
    commentCount: Int = MEMO_COMMENT_COUNT,
    createdAt: LocalDateTime = MEMO_CREATED_AT,
    updatedAt: LocalDateTime = MEMO_UPDATED_AT,
    fontName: String = FONT_NAME,
    fontStyle: String = FONT_STYLE,
    fontSize: Int = FONT_SIZE,
): Memo {
    val font = Font(fontName, fontStyle, fontSize)
    return Memo(memoId, writer, title, content, likeCount, commentCount, font, createdAt, updatedAt)
}

fun createMemoLike(
    memoLikeId: Long = MEMO_LIKE_ID,
    member: Member = createMember(),
    memo: Memo = createMemo(),
    createdAt: LocalDateTime = MEMO_LIKE_CREATED_AT,
): MemoLike {
    return MemoLike(memoLikeId, member, memo, createdAt)
}

fun createMemoSlice(): Slice<Memo> {
    return SliceImpl(listOf(createMemo(), createMemo(), createMemo()))
}