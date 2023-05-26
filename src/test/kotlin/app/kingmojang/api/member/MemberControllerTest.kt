package app.kingmojang.api.member

import app.kingmojang.api.RestControllerTest
import app.kingmojang.domain.member.api.MemberController
import app.kingmojang.domain.member.application.MemberService
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get

@WebMvcTest(MemberController::class)
class MemberControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var memberService: MemberService

    @MockkBean
    private lateinit var memoService: MemoService

    @Test
    fun `중복된 닉네임이 없는 경우 204 응답코드가 반환된다`() {
        every { memberService.existsNickname(NICKNAME, any()) } returns false

        mockMvc.get("/api/v1/members/nickname") {
            param("nickname", NICKNAME)
        }.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `중복된 닉네임이 있는 경우 409 응답코드가 반환된다`() {
        every { memberService.existsNickname(NICKNAME, any()) } returns true

        mockMvc.get("/api/v1/members/nickname") {
            param("nickname", NICKNAME)
        }.andExpect {
            status { isConflict() }
            content { success() }
        }
    }

    @Test
    fun `중복된 이메일이 없는 경우 204 응답코드가 반환된다`() {
        every { memberService.existsEmail(EMAIL) } returns false

        mockMvc.get("/api/v1/members/email") {
            param("email", EMAIL)
        }.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `중복된 이메일이 있는 경우 409 응답코드가 반환된다`() {
        every { memberService.existsEmail(EMAIL) } returns true

        mockMvc.get("/api/v1/members/email") {
            param("email", EMAIL)
        }.andExpect {
            status { isConflict() }
            content { success() }
        }
    }

}