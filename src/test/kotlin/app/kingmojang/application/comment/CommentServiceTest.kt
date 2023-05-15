package app.kingmojang.application.comment

import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.repository.CommentQueryRepository
import app.kingmojang.domain.comment.repository.CommentRepository
import app.kingmojang.domain.like.repository.CommentLikeRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.domain.memo.repository.MemoRepository
import app.kingmojang.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull

class CommentServiceTest : BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val memoRepository = mockk<MemoRepository>()
    val commentRepository = mockk<CommentRepository>()
    val commentQueryRepository = mockk<CommentQueryRepository>()
    val commentLikeRepository = mockk<CommentLikeRepository>()

    val commentService = CommentService(
        memberRepository,
        memoRepository,
        commentRepository,
        commentQueryRepository,
        commentLikeRepository
    )

    Given("정상적인 댓글 생성 요청이 있는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns member
        every { memoRepository.findByIdOrNull(MEMO_ID) } returns createMemo()
        every { commentRepository.save(any()) } returns createComment()

        When("댓글을 생성하면") {
            val actual = commentService.createComment(userPrincipal, MEMO_ID, createCommentRequest())
            Then("댓글이 생성된다") {
                actual shouldBe COMMENT_ID
            }
        }
    }

    Given("정상적인 댓글 수정 요청이 있는 경우") {
        val userPrincipal = UserPrincipal.create(createMember())
        every { commentRepository.findByIdOrNull(COMMENT_ID) } returns createComment()

        When("댓글을 수정하면") {
            val actual = commentService.updateComment(userPrincipal, COMMENT_ID, createCommentRequest())
            Then("댓글이 수정된다") {
                actual shouldBe COMMENT_ID
            }
        }
    }

    Given("정상적인 댓글 삭제 요청이 있는 경우") {
        val userPrincipal = UserPrincipal.create(createMember())
        val comment = createComment()
        every { commentRepository.findByIdOrNull(COMMENT_ID) } returns comment
        every { commentLikeRepository.findAllByCommentId(COMMENT_ID) } returns listOf(COMMENT_LIKE_ID)
        every { commentLikeRepository.deleteAllByIdInBatch(any()) } just Runs
        every { commentRepository.delete(comment) } just Runs

        When("댓글을 삭제하면") {
            val actual = commentService.deleteComment(userPrincipal, COMMENT_ID)
            Then("댓글이 삭제된다") {
                actual shouldBe Unit
            }
        }
    }

    Given("정상적인 메모의 댓글목록 조회 요청이 있는 경우") {
        every { commentQueryRepository.readCommentsInMemo(MEMO_ID, createCommonPageRequest()) } returns listOf(
            createCommentResponse(),
            createCommentResponse(),
            createCommentResponse()
        )


        When("해당 메모의 댓글 목록을 조회하면") {
            val actual = commentService.readComments(MEMO_ID, createCommonPageRequest())
            Then("댓글목록이 조회된다") {
                actual shouldBe createCommentsResponse()
            }
        }
    }

    Given("정상적인 댓글 좋아요 요청이 있는 경우") {
        val member = createMember()
        val userPrincipal = UserPrincipal.create(member)
        every { commentRepository.findByIdOrNull(COMMENT_ID) } returns createComment()
        every { memberRepository.findByIdOrNull(MEMBER_ID) } returns member
        every { commentLikeRepository.save(any()) } returns createCommentLike()

        When("댓글 좋아요를 하면") {
            val actual = commentService.increaseCommentLikeCount(userPrincipal, COMMENT_ID, MEMBER_ID)
            Then("댓글 좋아요가 생성된다") {
                actual shouldBe COMMENT_LIKE_ID
            }
        }
    }

    Given("정상적인 댓글 좋아요 취소 요청이 있는 경우") {
        val userPrincipal = UserPrincipal.create(createMember())
        val commentLike = createCommentLike()
        every { commentLikeRepository.findByCommentIdAndMemberId(COMMENT_ID, MEMBER_ID) } returns commentLike
        every { commentLikeRepository.delete(commentLike) } just Runs

        When("댓글 좋아요를 취소 하면") {
            val actual = commentService.decreaseCommentLikeCount(userPrincipal, COMMENT_ID, MEMBER_ID)
            Then("댓글 좋아요가 취소된다") {
                actual shouldBe Unit
            }
        }
    }

    afterTest {
        clearAllMocks(answers = false)
    }
})