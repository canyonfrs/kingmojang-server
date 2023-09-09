package app.kingmojang.domain.memo.api

import app.kingmojang.domain.comment.api.CommentSortType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import app.kingmojang.global.common.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class MemoController(
    private val memoService: MemoService,
) {

    @PostMapping("/memos")
    @PreAuthorize("hasRole('CREATOR')")
    fun createMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody @Valid request: MemoRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        val memoId = memoService.createMemo(memberId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/{id}")
            .buildAndExpand(memoId).toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(memoId))
    }

    @PatchMapping("/memos/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    fun updateMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable id: Long,
        @RequestBody @Valid request: MemoRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val memberId = userPrincipal.getId()
        val updateMemoId = memoService.updateMemo(id, memberId, request)
        return ResponseEntity.ok(CommonResponse.success(updateMemoId))
    }

    @GetMapping("/memos/{id}")
    fun readMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable id: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "created_at_asc") sort: String,
    ): ResponseEntity<CommonResponse<MemoResponse>> {
        val request = PageRequest.of(page, size, CommentSortType.of(sort).getSort())
        val memberId: Long? = userPrincipal?.getId()
        val response = memoService.readMemo(id, memberId, request)
        return ResponseEntity.ok(CommonResponse.success(response))
    }

    @DeleteMapping("/memos/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    fun deleteMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable id: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        val memberId = userPrincipal.getId()
        memoService.deleteMemo(id, memberId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @GetMapping("/memos")
    fun readMemosInUpdatedOrder(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<CommonResponse<MemosResponse>> {
        val request = PageRequest.of(0, size)
        val memberId = userPrincipal?.getId()
        val memos = memoService.readMemosInUpdatedOrder(memberId, request)
        return ResponseEntity.ok(CommonResponse.success(memos))
    }

    @GetMapping("/memos/subscriptions")
    fun readMemosInSubscribe(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<CommonResponse<MemosResponse>> {
        val request = PageRequest.of(page, size)
        val memberId = userPrincipal.getId()
        val memos = memoService.readMemosInSubscribe(memberId, request)
        return ResponseEntity.ok(CommonResponse.success(memos))
    }

    @GetMapping("/members/{memberId}/memos")
    fun readMemosWrittenByCreator(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable(name = "memberId") writerId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<CommonResponse<MemosResponse>> {
        val memberId = userPrincipal?.getId()
        val memos = memoService.readMemosWrittenByCreator(writerId, memberId, PageRequest.of(page, size))
        return ResponseEntity.ok(CommonResponse.success(memos))
    }
}