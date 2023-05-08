package app.kingmojang.api.comment

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.comment.api.CommentController
import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(CommentController::class)
class CommentControllerTest: RestControllerTest() {
    @MockkBean
    private lateinit var commentService: CommentService

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 댓글을 등록한다`() {
        // given
        every { commentService.createComment(any(), MEMO_ID, any()) } returns COMMENT_ID

        // when
        mockMvc.post("/api/v1/memos/$MEMO_ID/comments") {
            bearer(ACCESS_TOKEN)
            jsonContent(createCommentRequest())
        }.andExpect {
            status { isCreated() }
            header { exists("Location") }
            content { success(COMMENT_ID) }
        }
    }

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 댓글을 수정한다`() {
        // given
        every { commentService.updateComment(any(), COMMENT_ID, any()) } returns COMMENT_ID

        // when
        mockMvc.put("/api/v1/comments/$COMMENT_ID") {
            bearer(ACCESS_TOKEN)
            jsonContent(createCommentRequest())
        }.andExpect {
            status { isOk() }
            content { success(COMMENT_ID) }
        }
    }

    @Test
    @WithMockCustomUser(MemberType.USER)
    fun `성공적으로 댓글을 삭제한다`() {
        // given
        every { commentService.deleteComment(any(), COMMENT_ID) } just Runs

        // when
        mockMvc.delete("/api/v1/comments/$COMMENT_ID") {
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isOk() }
            content { success() }
        }
    }

    @Test
    fun `성공적으로 선택된 메모의 댓글 목록을 조회한다`() {
        // given
        val response = createCommentsResponse()
        every { commentService.readComments(MEMO_ID, any()) } returns response

        // when
        mockMvc.get("/api/v1/memos/$MEMO_ID/comments") {
            bearer(ACCESS_TOKEN)
            jsonContent(createCommonPageRequest(20, 0))
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }
}