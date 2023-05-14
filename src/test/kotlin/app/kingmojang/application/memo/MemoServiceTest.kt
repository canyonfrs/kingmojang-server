package app.kingmojang.application.memo

import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.like.repository.MemoLikeRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.domain.memo.repository.MemoQueryRepository
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class MemoServiceTest : BehaviorSpec({
    val memoRepository = mockk<MemoRepository>()
    val memoQueryRepository = mockk<MemoQueryRepository>()
    val commentQueryRepository = mockk<CommentQueryRepository>()
    val memberRepository = mockk<MemberRepository>()
    val memoLikeRepository = mockk<MemoLikeRepository>()

    val memoService =
        MemoService(memoRepository, memoQueryRepository, commentQueryRepository, memberRepository, memoLikeRepository)

    Given("정상적인 메모 생성 요청이 있는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        every { memberRepository.findByUsername(USERNAME) } returns member
        every { memoRepository.save(any()) } returns createMemo()

        When("메모를 생성하면") {
            val actual = memoService.createMemo(userPrincipal, createMemoRequest())
            Then("메모가 생성된다") {
                actual shouldBe MEMO_ID
            }
        }
    }

    Given("정상적인 메모 수정 요청이 있는 경우") {
        val userPrincipal = UserPrincipal.create(createMember())
        every { memoRepository.findByIdOrNull(MEMO_ID) } returns createMemo()

        When("메모를 수정하면") {
            val actual = memoService.updateMemo(userPrincipal, MEMO_ID, createMemoRequest())
            Then("메모가 수정된다") {
                actual shouldBe MEMO_ID
            }
        }
    }

    Given("비로그인 유저의 정상적인 메모 조회 요청이 있는 경우") {
        val commonPageRequest = createCommonPageRequest()
        every { memoQueryRepository.findByIdOrNull(MEMO_ID) } returns createMemoDto()
        every { commentQueryRepository.readCommentsInMemo(MEMO_ID, commonPageRequest) } returns listOf(
            createCommentResponse()
        )

        When("비로그인 유저가 메모를 조회하면") {
            val actual = memoService.readMemo(MEMO_ID, createCommonPageRequest())
            Then("메모가 조회된다") {
                actual.id shouldBe MEMO_ID
            }
        }
    }

    Given("로그인 유저의 정상적인 메모 조회 요청이 있는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        val commonPageRequest = createCommonPageRequest()
        every { memoQueryRepository.findByIdOrNullWithUsername(MEMO_ID, USERNAME) } returns createMemoDto()
        every {
            commentQueryRepository.readCommentsInMemoWithUsername(MEMO_ID, USERNAME, commonPageRequest)
        } returns listOf(createCommentResponse())

        When("비로그인 유저가 메모를 조회하면") {
            val actual = memoService.readMemoWithUsername(userPrincipal, MEMO_ID, createCommonPageRequest())
            Then("메모가 조회된다") {
                actual.id shouldBe MEMO_ID
            }
        }
    }

    Given("특정 유저가 작성한 메모 조회 요청이 있는 경우") {
        every { memoQueryRepository.findMemosInUpdatedOrder(SIZE.toInt()) } returns listOf(
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY)
        )

        When("특정 유저가 작성한 메모를 조회하면") {
            val actual = memoService.readMemosInUpdatedOrder(SIZE.toInt())
            Then("해당 메모가 조회된다") {
                actual shouldBe createMemosResponse()
            }
        }
    }

    Given("최근 업데이트된 메모 조회 요청이 있는 경우") {
        every { memoQueryRepository.findMemosInUpdatedOrder(SIZE.toInt()) } returns listOf(
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY),
            createMemoDto(content = SUMMARY)
        )

        When("특정 유저가 작성한 메모를 조회하면") {
            val actual = memoService.readMemosInUpdatedOrder(SIZE.toInt())
            Then("해당 메모가 조회된다") {
                actual shouldBe createMemosResponse()
            }
        }
    }
})