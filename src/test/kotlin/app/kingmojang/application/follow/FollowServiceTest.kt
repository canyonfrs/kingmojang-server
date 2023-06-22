package app.kingmojang.application.follow

import app.kingmojang.domain.follow.application.FollowService
import app.kingmojang.domain.follow.exception.NotFoundFollowException
import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.fixture.*
import app.kingmojang.global.exception.CommonException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull

class FollowServiceTest : BehaviorSpec({
    val followRepository = mockk<FollowRepository>()
    val memberRepository = mockk<MemberRepository>()

    val followService = FollowService(memberRepository, followRepository)

    Given("정상적인 팔로우 요청이 들어오는경우") {
        val creator = createMember(memberId = CREATOR_ID)
        val follower = createMember(memberId = FOLLOWER_ID)

        every { memberRepository.findByIdOrNull(CREATOR_ID) } returns creator
        every { memberRepository.findByIdOrNull(FOLLOWER_ID) } returns follower
        every { followRepository.existsByCreatorIdAndFollowerId(CREATOR_ID, FOLLOWER_ID) } returns false
        every { followRepository.save(any()) } returns createFollow()


        When("팔로우를 신청한다면") {
            val actual = followService.createFollow(FOLLOWER_ID, CREATOR_ID)

            Then("팔로우가 생성된다") {
                actual shouldBe FOLLOW_ID
            }
        }
    }

    Given("정상적인 팔로우 취소 요청이 들어오는 경우") {
        val follow = createFollow()

        every { followRepository.findByCreatorIdAndFollowerId(CREATOR_ID, FOLLOWER_ID) } returns follow
        every { followRepository.delete(follow) } just Runs

        When("팔로우를 취소한다면") {
            val actual = followService.deleteFollow(FOLLOWER_ID, CREATOR_ID)

            Then("팔로우가 취소된다") {
                actual shouldBe Unit
            }
        }
    }

    Given("이미 팔로우한 유저가 팔로우 요청을 하는 경우") {
        every { followRepository.existsByCreatorIdAndFollowerId(CREATOR_ID, FOLLOWER_ID) } returns true

        When("팔로우를 신청한다면") {
            Then("예외가 발생한다") {
                shouldThrow<CommonException> {
                    followService.createFollow(FOLLOWER_ID, CREATOR_ID)
                }
            }
        }
    }

    Given("팔로우가 존재하지 않는 경우") {
        every { followRepository.findByCreatorIdAndFollowerId(CREATOR_ID, FOLLOWER_ID) } returns null

        When("팔로우를 취소한다면") {
            Then("예외가 발생한다") {
                shouldThrow<NotFoundFollowException> {
                    followService.deleteFollow(FOLLOWER_ID, CREATOR_ID)
                }
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})