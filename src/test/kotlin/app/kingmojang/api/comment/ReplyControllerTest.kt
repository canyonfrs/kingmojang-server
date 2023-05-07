package app.kingmojang.api.comment

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.comment.api.ReplyController
import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(ReplyController::class)
class ReplyControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var replyService: ReplyService

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 답글을 등록한다`() {
        every { replyService.createReply(any(), MEMO_ID, COMMENT_ID, any()) } returns REPLY_ID

        mockMvc.post("/api/v1/memos/$MEMO_ID/comments/$COMMENT_ID/replies") {
            bearer(ACCESS_TOKEN)
            jsonContent(createReplyRequest())
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            content { success(REPLY_ID) }
        }
    }

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 답글을 수정한다`() {
        every { replyService.updateReply(any(), REPLY_ID, any()) } returns REPLY_ID

        mockMvc.put("/api/v1/replies/$REPLY_ID") {
            bearer(ACCESS_TOKEN)
            jsonContent(createReplyRequest())
        }.andExpect {
            status { isOk() }
            content { success(REPLY_ID) }
        }
    }

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 답글을 삭제한다`() {
        every { replyService.deleteReply(any(), REPLY_ID) } just Runs

        mockMvc.delete("/api/v1/replies/$REPLY_ID") {
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isOk() }
            content { success() }
        }
    }
}