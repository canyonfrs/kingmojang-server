package app.kingmojang.api.member

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.member.api.CreatorInfoController
import app.kingmojang.domain.member.application.CreatorInfoService
import app.kingmojang.domain.member.dto.response.CreatorInfoResponse
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@WebMvcTest(CreatorInfoController::class)
class CreatorInfoControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var creatorInfoService: CreatorInfoService

    @Test
    @WithMockCustomUser
    fun `성공적으로 크리에이터 회원의 정보를 입력한다`() {
        every { creatorInfoService.createCreatorInfo(any(), MEMBER_ID, any()) } returns CREATOR_INFO_ID

        mockMvc.post("/api/v1/members/$MEMBER_ID/creator-infos") {
            jsonContent(createCreatorInfoRequest())
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isCreated() }
            content { success(CREATOR_INFO_ID) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 크리에이터 회원의 정보를 수정한다`() {
        every { creatorInfoService.updateCreatorInfo(any(), MEMBER_ID, any()) } returns CREATOR_INFO_ID

        mockMvc.put("/api/v1/members/$MEMBER_ID/creator-infos") {
            jsonContent(createCreatorInfoRequest())
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isOk() }
            content { success(CREATOR_INFO_ID) }
        }
    }

    @Test
    fun `성공적으로 크리에이터 회원의 정보를 조회한다`() {
        val response = createCreatorInfoResponse()
        every { creatorInfoService.readCreatorInformation(any(), MEMBER_ID) } returns response

        mockMvc.get("/api/v1/members/$MEMBER_ID/creator-infos") {
        }.andExpect {
            status { isOk() }
            content { success(response) }
        }
    }
}