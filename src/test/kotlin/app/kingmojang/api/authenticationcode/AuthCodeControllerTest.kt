package app.kingmojang.api.authenticationcode

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.authenticationcode.api.AuthCodeController
import app.kingmojang.domain.authenticationcode.application.AuthCodeService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.post

@WebMvcTest(AuthCodeController::class)
class AuthCodeControllerTest : RestControllerTest() {

    @MockkBean
    private lateinit var authCodeService: AuthCodeService

    @Test
    @WithMockCustomUser(type =  MemberType.ADMIN)
    fun `성공적으로 인증코드를 생성한다`() {
        every { authCodeService.createAuthCode(any(), EMAIL) } returns createAuthCodeResponse()

        mockMvc.post("/api/v1/auth-codes") {
            bearer(ACCESS_TOKEN)
            param("email", EMAIL)
        }.andExpect {
            status { isCreated() }
            content { success() }
        }
    }

}