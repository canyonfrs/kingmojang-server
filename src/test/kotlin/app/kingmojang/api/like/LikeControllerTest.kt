package app.kingmojang.api.like

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.like.api.LikeController
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest(LikeController::class)
class LikeControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var memoService: MemoService

    @MockkBean
    private lateinit var commentService: CommentService

    @MockkBean
    private lateinit var replyService: ReplyService

    @Test
    @WithMockCustomUser
    fun `성공적으로 메모에 좋아요를 등록한다`() {
        every { memoService.increaseMemoLikeCount(MEMO_ID, MEMBER_ID) } returns MEMO_LIKE_ID

        mockMvc.post("/api/v1/memos/$MEMO_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 메모에 좋아요를 취소한다`() {
        every { memoService.decreaseMemoLikeCount(MEMO_ID, MEMBER_ID) } just Runs

        mockMvc.delete("/api/v1/memos/$MEMO_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isOk() }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 댓글에 좋아요를 등록한다`() {
        every { commentService.increaseCommentLikeCount(COMMENT_ID, MEMBER_ID) } returns COMMENT_LIKE_ID

        mockMvc.post("/api/v1/comments/$COMMENT_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 댓글에 좋아요를 취소한다`() {
        every { commentService.decreaseCommentLikeCount(COMMENT_ID, MEMBER_ID) } just Runs

        mockMvc.delete("/api/v1/comments/$COMMENT_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isOk() }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 답글에 좋아요를 등록한다`() {
        every { replyService.increaseReplyLikeCount(REPLY_ID, MEMBER_ID) } returns REPLY_ID

        mockMvc.post("/api/v1/replies/$REPLY_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 답글에 좋아요를 취소한다`() {
        every { replyService.decreaseReplyLikeCount(REPLY_ID, MEMBER_ID) } just Runs

        mockMvc.delete("/api/v1/replies/$REPLY_ID/likes") {
            bearer(ACCESS_TOKEN)
            param("memberId", MEMBER_ID.toString())
        }.andExpect {
            status { isOk() }
            content { success() }
        }
    }

}