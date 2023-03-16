package app.kingmojang.domain.like.api

import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class LikeController(
    private val memoService: MemoService,
    private val commentService: CommentService,
    private val replyService: ReplyService,
) {
    @PostMapping("/memos/{memoId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseMemoLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable memoId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        val likeId = memoService.increaseMemoLikeCount(userDetails, memoId, username)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/$memoId/like/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/memos/{memoId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseMemoLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable memoId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        memoService.decreaseMemoLikeCount(userDetails, memoId, username)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @PostMapping("/comments/{commentId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseCommentLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable commentId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        val likeId = commentService.increaseCommentLikeCount(userDetails, commentId, username)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/comments/$commentId/like/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/comments/{commentId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseCommentLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable commentId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        commentService.decreaseCommentLikeCount(userDetails, commentId, username)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @PostMapping("/replies/{replyId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun increaseReplyLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable replyId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        val likeId = replyService.increaseReplyLikeCount(userDetails, replyId, username)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/replies/$replyId/like/$likeId")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @DeleteMapping("/replies/{replyId}/like")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun decreaseReplyLikeCount(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable replyId: Long,
        @RequestParam username: String,
    ): ResponseEntity<CommonResponse<Void>> {
        replyService.decreaseReplyLikeCount(userDetails, replyId, username)
        return ResponseEntity.ok(CommonResponse.success())
    }
}