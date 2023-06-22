package app.kingmojang.application.comment

import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.comment.repository.ReplyRepository
import app.kingmojang.domain.like.repository.ReplyLikeRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull

class ReplyServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val memoRepository = mockk<MemoRepository>()
    val commentRepository = mockk<CommentRepository>()
    val replyRepository = mockk<ReplyRepository>()
    val replyLikeRepository = mockk<ReplyLikeRepository>()

    val replyService = ReplyService(
        memberRepository,
        memoRepository,
        commentRepository,
        replyRepository,
        replyLikeRepository
    )

    Given("정상적인 답글 생성 요청이 있는 경우") {
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns createMember()
        every { memoRepository.findByIdOrNull(MEMO_ID) } returns createMemo()
        every { commentRepository.findByIdOrNull(COMMENT_ID) } returns createComment()
        every { replyRepository.save(any()) } returns createReply()

        When("답글을 생성하면") {
            val actual = replyService.createReply(MEMO_ID, COMMENT_ID, MEMBER_ID, createReplyRequest())
            Then("답글이 생성된다") {
                actual shouldBe REPLY_ID
            }
        }
    }

    Given("정상적인 답글 수정 요청이 있는 경우") {
        every { replyRepository.findByIdOrNull(REPLY_ID) } returns createReply()

        When("답글을 수정하면") {
            val actual = replyService.updateReply(REPLY_ID, MEMBER_ID, createReplyRequest())
            Then("답글이 수정된다") {
                actual shouldBe REPLY_ID
            }
        }
    }

    Given("정상적인 답글 삭제 요청이 있는 경우") {
        val reply = createReply()
        every { replyRepository.findByIdOrNull(REPLY_ID) } returns reply
        every { replyLikeRepository.findAllByReplyId(REPLY_ID) } returns listOf(REPLY_LIKE_ID)
        every { replyLikeRepository.deleteAllByIdInBatch(any()) } just Runs
        every { replyRepository.delete(reply) } just Runs

        When("댓글을 삭제하면") {
            val actual = replyService.deleteReply(REPLY_ID, MEMBER_ID)
            Then("댓글이 삭제된다") {
                actual shouldBe Unit
            }
        }
    }


    Given("정상적인 답글 좋아요 요청이 있는 경우") {
        every { replyRepository.findByIdOrNull(REPLY_ID) } returns createReply()
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns createMember()
        every { replyLikeRepository.save(any()) } returns createReplyLike()

        When("답글 좋아요를 하면") {
            val actual = replyService.increaseReplyLikeCount(REPLY_ID, MEMBER_ID)
            Then("답글 좋아요가 생성된다") {
                actual shouldBe REPLY_LIKE_ID
            }
        }
    }

    Given("정상적인 답글 좋아요 취소 요청이 있는 경우") {
        val replyLike = createReplyLike()
        every { replyLikeRepository.findByReplyIdAndMemberId(REPLY_ID, MEMBER_ID) } returns replyLike
        every { replyLikeRepository.delete(replyLike) } just Runs

        When("답글 좋아요를 취소 하면") {
            val actual = replyService.decreaseReplyLikeCount(REPLY_ID, MEMBER_ID)
            Then("답글 좋아요가 취소된다") {
                actual shouldBe Unit
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})