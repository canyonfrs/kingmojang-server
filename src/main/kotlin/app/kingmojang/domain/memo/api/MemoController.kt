package app.kingmojang.domain.memo.api

import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.memo.application.MemoService
import app.kingmojang.domain.memo.dto.request.MemoRequest
import app.kingmojang.domain.memo.dto.response.MemoResponse
import app.kingmojang.domain.memo.dto.response.MemosResponse
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.common.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1/memos")
class MemoController(
    private val memoService: MemoService,
) {

    @PostMapping
    @PreAuthorize("hasRole('CREATOR')")
    fun createMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @RequestBody @Valid request: MemoRequest,
    ): ResponseEntity<CommonResponse<Void>> {
        val id = memoService.createMemo(userPrincipal, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/memos/{id}")
            .buildAndExpand(id).toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success())
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CREATOR')")
    fun updateMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable id: Long,
        @RequestBody @Valid request: MemoRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        val updateMemoId = memoService.updateMemo(userPrincipal, id, request)
        return ResponseEntity.ok(CommonResponse.success(updateMemoId))
    }

    @GetMapping("/{id}")
    fun readMemo(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable id: Long,
        @RequestParam(defaultValue = "10") size: Long,
        @RequestParam(defaultValue = "0") page: Long,
    ): ResponseEntity<CommonResponse<MemoResponse>> {
        val request = CommonPageRequest(size, page)
        val memo =
            if (userPrincipal == null) memoService.readMemo(id, request)
            else memoService.readMemoWithUsername(userPrincipal, id, request)

        return ResponseEntity.ok(CommonResponse.success(memo))
    }

    @GetMapping
    fun readMemoSummaries(@RequestParam(defaultValue = "5") size: Int): ResponseEntity<CommonResponse<MemosResponse>> {
        val memos = memoService.readMemosInUpdatedOrder(size)
        return ResponseEntity.ok(CommonResponse.success(memos))
    }

}