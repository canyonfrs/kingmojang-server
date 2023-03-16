package app.kingmojang.domain.comment.api

import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class CommentController(
    private val commentService: CommentService,
) {
    @PostMapping("/memos/{memoId}/comments")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun createComment(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable memoId: Long,
        @RequestBody request: CommentRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val commentId = commentService.createComment(userDetails, memoId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/${memoId}/comments/${commentId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(commentId))
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun updateComment(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable commentId: Long,
        @RequestBody request: CommentRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        return ResponseEntity.ok(CommonResponse.success(commentService.updateComment(userDetails, commentId, request)))
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteComment(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable commentId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        commentService.deleteComment(userDetails, commentId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @GetMapping("/memos/{memoId}/comments")
    fun readComments(
        @PathVariable memoId: Long,
        @RequestParam(defaultValue = "20") size: Long,
        @RequestParam(defaultValue = "0") page: Long,
    ): ResponseEntity<CommonResponse<CommentsResponse>> {
        val request = CommonPageRequest(size, page)
        return ResponseEntity.ok(CommonResponse.success(commentService.readComments(memoId, request)))
    }
}