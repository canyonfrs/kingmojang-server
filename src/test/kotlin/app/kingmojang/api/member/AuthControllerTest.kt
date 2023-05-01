package app.kingmojang.api.member

import app.kingmojang.api.RestControllerTest
import app.kingmojang.domain.member.api.AuthController
import app.kingmojang.domain.member.application.AuthService
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthController::class)
class AuthControllerTest: RestControllerTest() {
    @MockkBean
    private lateinit var authService: AuthService

    @Test
    fun `올바른 회원가입 요청 시 성공적으로 회원가입한다`() {
        every { authService.signup(any()) } returns MEMBER_ID

        mockMvc.post("/api/v1/members") {
            jsonContent(createSignupRequest())
        }.andExpect {
            status { isCreated() }
            content { success() }
        }
    }

    @Test
    fun `올바른 로그인 요청 응답으로 토큰을 생성 후 반환`() {
        val response = createTokenResponse()
        every { authService.login(any()) } returns response

        mockMvc.post("/api/v1/login") {
            jsonContent(createLoginRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }

    @Test
    fun `리프레시 토큰으로 액세스 토큰을 갱신한다`() {
        val response = createTokenResponse()
        every { authService.refresh(any()) } returns response

        mockMvc.post("/api/v1/refresh") {
            jsonContent(createRefreshRequest())
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }
}