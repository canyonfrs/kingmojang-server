package app.kingmojang.domain.like.api

import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class LikeController(
    private val memoService: MemoService,
    private val commentService: CommentService,
    private val replyService: ReplyService,
) {
    @PostMapping("/memos/{memoId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseMemoLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memoId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        val likeId = memoService.increaseMemoLikeCount(memoId, memberId)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/$memoId/likes/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/memos/{memoId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseMemoLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memoId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        memoService.decreaseMemoLikeCount(memoId, memberId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @PostMapping("/comments/{commentId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseCommentLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        val likeId = commentService.increaseCommentLikeCount(commentId, memberId)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/comments/$commentId/likes/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/comments/{commentId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseCommentLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        commentService.decreaseCommentLikeCount(commentId, memberId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @PostMapping("/replies/{replyId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseReplyLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable replyId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        val likeId = replyService.increaseReplyLikeCount(replyId, memberId)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/replies/$replyId/likes/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/replies/{replyId}/likes")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseReplyLikeCount(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable replyId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        replyService.decreaseReplyLikeCount(replyId, memberId)
        return ResponseEntity.ok(CommonResponse.success())
    }
}