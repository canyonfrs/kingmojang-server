package app.kingmojang.api.follow

import app.kingmojang.annotation.WithMockCustomUser
import app.kingmojang.api.RestControllerTest
import app.kingmojang.api.bearer
import app.kingmojang.domain.follow.api.FollowController
import app.kingmojang.domain.follow.application.FollowService
import app.kingmojang.fixture.*
import com.ninjasquad.springmockk.MockkBean
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

@WebMvcTest(FollowController::class)
class FollowControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var followService: FollowService

    @Test
    @WithMockCustomUser
    fun `성공적으로 팔로우를 요청한다`() {
        every { followService.createFollow(any(), FOLLOWER_ID, CREATOR_ID) } returns FOLLOW_ID

        mockMvc.post("/api/v1/follows/$FOLLOWER_ID/to/$CREATOR_ID") {
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isCreated() }
            content { success(FOLLOW_ID) }
        }
    }

    @Test
    @WithMockCustomUser
    fun `성공적으로 팔로우를 취소한다`() {
        every { followService.deleteFollow(any(), FOLLOWER_ID, CREATOR_ID) } just Runs

        mockMvc.delete("/api/v1/follows/$FOLLOWER_ID/to/$CREATOR_ID") {
            bearer(ACCESS_TOKEN)
        }.andExpect {
            status { isOk() }
            content { success() }
        }


    }

}