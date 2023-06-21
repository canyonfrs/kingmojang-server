package app.kingmojang.domain.comment.api

import app.kingmojang.domain.comment.application.CommentService
import app.kingmojang.domain.comment.dto.request.CommentRequest
import app.kingmojang.domain.comment.dto.response.CommentsResponse
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memoId: Long,
        @RequestBody request: CommentRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        val commentId = commentService.createComment(memoId, memberId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/${memoId}/comments/${commentId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(commentId))
    }

    @PatchMapping("/comments/{commentId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun updateComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long,
        @RequestBody request: CommentRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        return ResponseEntity.ok(CommonResponse.success(commentService.updateComment(commentId, memberId, request)))
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteComment(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable commentId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        commentService.deleteComment(commentId, memberId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @GetMapping("/memos/{memoId}/comments")
    fun readComments(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable memoId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(defaultValue = "created_at_asc") sort: String,
    ): ResponseEntity<CommonResponse<CommentsResponse>> {
        val memberId = userPrincipal?.getId()
        val request = PageRequest.of(page, size, CommentSortType.of(sort).getSort())
        return ResponseEntity.ok(CommonResponse.success(commentService.readComments(memoId, memberId, request)))
    }

    @GetMapping("/members/{memberId}/comments")
    fun readCommentsByMember(
        @PathVariable memberId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CommonResponse<CommentsResponse>> {
        val request = PageRequest.of(page, size)
        return ResponseEntity.ok(CommonResponse.success(commentService.readCommentsByMember(memberId, request)))
    }
}