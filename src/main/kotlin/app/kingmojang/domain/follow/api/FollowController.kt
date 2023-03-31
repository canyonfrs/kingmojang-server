package app.kingmojang.domain.follow.api

import app.kingmojang.domain.follow.application.FollowService
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.global.common.response.CommonResponse
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
        val followId = followService.createFollow(userPrincipal, followerId, creatorId)

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
        followService.deleteFollow(userPrincipal, followerId, creatorId)
        return ResponseEntity.ok(CommonResponse.success())
    }

}