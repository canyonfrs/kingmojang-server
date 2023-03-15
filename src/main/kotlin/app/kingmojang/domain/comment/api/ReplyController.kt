package app.kingmojang.domain.comment.api

import app.kingmojang.domain.comment.application.ReplyService
import app.kingmojang.domain.comment.dto.request.ReplyRequest
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class ReplyController(
    private val replyService: ReplyService,
) {
    @PostMapping("/memos/{memoId}/comments/{commentId}/replies")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun createReply(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable memoId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: ReplyRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val replyId = replyService.createReply(userDetails, memoId, commentId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/${memoId}/comments/${commentId}/replies/${replyId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(replyId))
    }

    @PutMapping("/replies/{replyId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun updateReply(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable replyId: Long,
        @RequestBody request: ReplyRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        return ResponseEntity.ok().body(CommonResponse.success(replyService.updateReply(userDetails, replyId, request)))
    }

    @DeleteMapping("/replies/{replyId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteReply(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable replyId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        replyService.deleteReply(userDetails, replyId)
        return ResponseEntity.ok().body(CommonResponse.success())
    }

}