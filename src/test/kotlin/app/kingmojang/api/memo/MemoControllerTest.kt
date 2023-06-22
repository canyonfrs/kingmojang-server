package app.kingmojang.api.memo

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.memo.api.MemoController
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(MemoController::class)
class MemoControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var memoService: MemoService

    @Test
    @WithMockCustomUser
    fun `성공적으로 메모를 등록한다`() {
        every { memoService.createMemo(any(), any()) } returns MEMO_ID

        mockMvc.post("/api/v1/memos") {
            bearer(ACCESS_TOKEN)
            jsonContent(createMemoRequest())
        }.andExpect {
            status { isCreated() }
            content { success() }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 메모를 수정한다`() {
        every { memoService.updateMemo(any(), MEMO_ID, any()) } returns MEMO_ID

        mockMvc.patch("/api/v1/memos/$MEMO_ID") {
            bearer(ACCESS_TOKEN)
            jsonContent(createMemoRequest())
        }.andExpect {
            status { isOk() }
            content { success(MEMO_ID) }
        }
    }

    @Test
    fun `비회원으로 성공적으로 메모를 조회한다`() {
        val response = createMemoResponse()
        every { memoService.readMemo(MEMO_ID, null, any()) } returns response

        mockMvc.get("/api/v1/memos/$MEMO_ID") {
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    @WithMockCustomUser(type = MemberType.USER)
    fun `회원으로 성공적으로 메모를 조회한다`() {
        val response = createMemoResponse(isLike = true)
        every { memoService.readMemo(MEMO_ID, MEMBER_ID, any()) } returns response

        mockMvc.get("/api/v1/memos/$MEMO_ID") {
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `성공적으로 최근 업데이트된 메모장을 조회한다`() {
        val response = createMemosResponse()
        every {
            memoService.readMemosInUpdatedOrder(null, any())
        } returns response

        mockMvc.get("/api/v1/memos") {
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }
}