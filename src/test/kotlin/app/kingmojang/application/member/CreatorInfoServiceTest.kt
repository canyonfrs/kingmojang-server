package app.kingmojang.application.member

import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.application.CreatorInfoService
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.repository.CreatorInfoRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class CreatorInfoServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val creatorInfoRepository = mockk<CreatorInfoRepository>()
    val followRepository = mockk<FollowRepository>()

    val creatorInfoService = CreatorInfoService(memberRepository, creatorInfoRepository, followRepository)

    Given("정상적인 크리에이터 정보를 생성하는 요청을 보내는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        val creatorInfo = createCreatorInfo()
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns member
        every { creatorInfoRepository.save(any()) } returns creatorInfo

        When("해당 요청으로 크리에이터 정보를 생성하면") {
            val actual = creatorInfoService.createCreatorInfo(userPrincipal, MEMBER_ID, createCreatorInfoRequest())
            Then("생성된 크리에이터 정보의 아이디가 반환된다") {
                actual shouldBe CREATOR_INFO_ID
            }
        }
    }

    Given("정상적인 크리에이터 정보를 수정하는 요청을 보내는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        val creatorInfo = createCreatorInfo()
        member.createCreatorInformation(creatorInfo)
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns member

        When("해당 요청으로 크리에이터 정보를 수정하면") {
            val actual = creatorInfoService.updateCreatorInfo(userPrincipal, MEMBER_ID, createCreatorInfoRequest(
                UPDATE_INTRODUCE, UPDATE_BROADCAST_LINK, UPDATE_YOUTUBE, UPDATE_DONATION_LINK
            ))
            Then("수정된 크리에이터 정보의 아이디가 반환된다") {
                actual shouldBe CREATOR_INFO_ID
                creatorInfo.introduce shouldBe UPDATE_INTRODUCE
                creatorInfo.broadcastLink shouldBe UPDATE_BROADCAST_LINK
                creatorInfo.youtube shouldBe UPDATE_YOUTUBE
                creatorInfo.donationLink shouldBe UPDATE_DONATION_LINK
            }
        }
    }

    Given("로그인한 유저가 크리에이터 정보를 조회하는 요청을 보내는 경우") {
        val creatorInfo = createCreatorInfo()
        val creator = createMember()
        creator.createCreatorInformation(creatorInfo)
        val member = createMember(memberId = 2L, memberType = MemberType.USER)
        val userPrincipal = UserPrincipal.create(member)

        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns creator
        every { followRepository.existsByCreatorIdAndFollowerId(MEMBER_ID, 2L) } returns true

        When("해당 요청으로 크리에이터 정보를 조회하면") {
            val actual = creatorInfoService.readCreatorInformation(userPrincipal, MEMBER_ID)

            Then("크리에이터 정보가 반환된다") {
                actual shouldBe createCreatorInfoResponse(isFollow = true)
            }
        }
    }

    Given("비로그인한 유저가 크리에이터 정보를 조회하는 요청을 보내는 경우") {
        val creatorInfo = createCreatorInfo()
        val creator = createMember()
        creator.createCreatorInformation(creatorInfo)

        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns creator

        When("해당 요청으로 크리에이터 정보를 조회하면") {
            val actual = creatorInfoService.readCreatorInformation(null, MEMBER_ID)

            Then("크리에이터 정보가 반환된다") {
                actual shouldBe createCreatorInfoResponse()
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})