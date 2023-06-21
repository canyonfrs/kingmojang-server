package app.kingmojang.domain.comment.api

import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.domain.comment.dto.response.RepliesResponse
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
class ReplyController(
    private val replyService: ReplyService,
) {
    @PostMapping("/memos/{memoId}/comments/{commentId}/replies")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun createReply(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memoId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: ReplyRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        val replyId = replyService.createReply(memoId, commentId, memberId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/${memoId}/comments/${commentId}/replies/${replyId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(replyId))
    }

    @PutMapping("/replies/{replyId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun updateReply(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable replyId: Long,
        @RequestBody request: ReplyRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        return ResponseEntity.ok().body(CommonResponse.success(replyService.updateReply(replyId, memberId, request)))
    }

    @DeleteMapping("/replies/{replyId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteReply(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable replyId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        replyService.deleteReply(replyId, memberId)
        return ResponseEntity.ok().body(CommonResponse.success())
    }

    @GetMapping("/comments/{commentId}/replies")
    fun readReplies(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable commentId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<CommonResponse<RepliesResponse>> {
        val memberId = userPrincipal?.getId()
        val request = PageRequest.of(page, size)
        val replies = replyService.readReplies(commentId, memberId, request)
        return ResponseEntity.ok().body(CommonResponse.success(replies))
    }

    @GetMapping("/members/{memberId}/replies")
    fun readRepliesByMember(
        @PathVariable memberId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ) : ResponseEntity<CommonResponse<RepliesResponse>> {
        val request = PageRequest.of(page, size)
        return ResponseEntity.ok().body(CommonResponse.success(replyService.readRepliesByMember(memberId, request)))
    }
}