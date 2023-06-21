package app.kingmojang.domain.follow.api

import app.kingmojang.domain.follow.application.FollowService
import app.kingmojang.domain.follow.dto.response.FollowResponse
import app.kingmojang.domain.follow.dto.response.FollowsResponse
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.common.request.CommonPageRequest
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/v1")
class FollowController(
    private val followService: FollowService,
) {
    @PostMapping("/follows/{followerId}/to/{creatorId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun createFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable followerId: Long,
        @PathVariable creatorId: Long,
    ): ResponseEntity<CommonResponse<Long>> {
        if (!userPrincipal.isValidMember(followerId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        val followId = followService.createFollow(followerId, creatorId)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/follows/${followId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(followId))
    }

    @DeleteMapping("/follows/{followerId}/to/{creatorId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteFollow(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable followerId: Long,
        @PathVariable creatorId: Long,
    ): ResponseEntity<CommonResponse<Void>> {
        if (!userPrincipal.isValidMember(followerId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        followService.deleteFollow(followerId, creatorId)
        return ResponseEntity.ok(CommonResponse.success())
    }

    @GetMapping("/members/{memberId}/follows")
    fun readFollowByFollower(
        @PathVariable memberId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
    ): ResponseEntity<CommonResponse<FollowsResponse>> {
        val request = PageRequest.of(page, size)
        return ResponseEntity.ok(CommonResponse.success(followService.readFollowByFollower(memberId, request)))
    }
}