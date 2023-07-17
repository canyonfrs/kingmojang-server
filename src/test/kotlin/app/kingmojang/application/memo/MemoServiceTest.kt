package app.kingmojang.application.memo

import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.like.repository.CommentLikeRepository
import app.kingmojang.domain.like.repository.MemoLikeRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

class MemoServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val memoRepository = mockk<MemoRepository>()
    val memoLikeRepository = mockk<MemoLikeRepository>()
    val commentRepository = mockk<CommentRepository>()
    val commentLikeRepository = mockk<CommentLikeRepository>()
    val followRepository = mockk<FollowRepository>()

    val memoService = MemoService(
        memberRepository,
        memoRepository,
        memoLikeRepository,
        commentRepository,
        commentLikeRepository,
        followRepository
    )

    Given("정상적인 메모 생성 요청이 있는 경우") {
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns createMember()
        every { memoRepository.save(any()) } returns createMemo()

        When("메모를 생성하면") {
            val actual = memoService.createMemo(MEMBER_ID, createMemoRequest())
            Then("메모가 생성된다") {
                actual shouldBe MEMO_ID
            }
        }
    }

    Given("정상적인 메모 수정 요청이 있는 경우") {
        every { memoRepository.findMemoWithWriterByIdAndDeletedFalse(MEMO_ID) } returns createMemo()

        When("메모를 수정하면") {
            val actual = memoService.updateMemo(MEMO_ID, MEMBER_ID, createMemoRequest())
            Then("메모가 수정된다") {
                actual shouldBe MEMO_ID
            }
        }
    }

    Given("비로그인 유저의 정상적인 메모 조회 요청이 있는 경우") {
        val request = PageRequest.of(0, 10)
        every { memoRepository.findMemoWithWriterByIdAndDeletedFalse(MEMO_ID) } returns createMemo()
        every {
            commentRepository.findAllWithWriterByMemoIdAndMemoDeletedFalseAndDeletedFalse(
                MEMO_ID,
                request
            )
        } returns createCommentPages()

        When("비로그인 유저가 메모를 조회하면") {
            val actual = memoService.readMemo(MEMO_ID, null, request)
            Then("메모가 조회된다") {
                actual.id shouldBe MEMO_ID
            }
        }
    }

    Given("로그인 유저의 정상적인 메모 조회 요청이 있는 경우") {
        val request = PageRequest.of(0, 10)
        every { memoRepository.findMemoWithWriterByIdAndDeletedFalse(MEMO_ID) } returns createMemo()
        every {
            commentRepository.findAllWithWriterByMemoIdAndMemoDeletedFalseAndDeletedFalse(MEMO_ID, request)
        } returns createCommentPages()

        When("비로그인 유저가 메모를 조회하면") {
            val actual = memoService.readMemo(MEMO_ID, null, createPageRequest())
            Then("메모가 조회된다") {
                actual.id shouldBe MEMO_ID
            }
        }
    }

    Given("특정 유저가 작성한 메모 조회 요청이 있는 경우") {
        every {
            memoRepository.findAllWithWriterByWriterIdAndDeletedFalse(MEMBER_ID, any())
        } returns createMemoSlice()

        When("특정 유저가 작성한 메모를 조회하면") {
            val actual = memoService.readMemosWrittenByCreator(MEMBER_ID, null, createPageRequest())
            Then("해당 메모가 조회된다") {
                actual shouldBe createMemosResponse()
            }
        }
    }

    Given("최근 업데이트된 메모 조회 요청이 있는 경우") {
        every { memoRepository.findAllWithWriterByDeletedFalse(any()) } returns createMemoSlice()

        When("특정 유저가 작성한 메모를 조회하면") {
            val actual = memoService.readMemosInUpdatedOrder(null, createPageRequest())
            Then("해당 메모가 조회된다") {
                actual shouldBe createMemosResponse()
            }
        }
    }

    Given("정상적인 메모 좋아요 요청이 있는 경우") {
        val member = createMember()
        every { memoRepository.findByIdOrNull(MEMO_ID) } returns createMemo()
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns member
        every { memoLikeRepository.save(any()) } returns createMemoLike()

        When("메모 좋아요를 하면") {
            val actual = memoService.increaseMemoLikeCount(MEMO_ID, MEMBER_ID)
            Then("메모 좋아요가 생성된다") {
                actual shouldBe MEMO_LIKE_ID
            }
        }
    }

    Given("정상적인 메모 좋아요 취소 요청이 있는 경우") {
        val memoLike = createMemoLike()
        every { memoLikeRepository.findByMemoIdAndMemberId(MEMO_ID, MEMBER_ID) } returns memoLike
        every { memoLikeRepository.delete(memoLike) } just Runs

        When("메모 좋아요를 취소 하면") {
            val actual = memoService.decreaseMemoLikeCount(MEMO_ID, MEMBER_ID)
            Then("메모 좋아요가 취소된다") {
                actual shouldBe Unit
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})