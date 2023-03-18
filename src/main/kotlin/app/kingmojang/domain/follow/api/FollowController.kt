package app.kingmojang.domain.follow.api

import app.kingmojang.domain.follow.application.FollowService
import app.kingmojang.global.common.response.CommonResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
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
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam followerId: Long,
        @RequestParam creatorId: Long,
    ): ResponseEntity<CommonResponse<Long>> {
        val followId = followService.createFollow(userDetails, followerId, creatorId)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/follows/${followId}")
            .buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(followId))

    }

    @DeleteMapping("/follows/{followerId}/to/{creatorId}")
    @PreAuthorize("hasRole({'USER', 'CREATOR'})")
    fun deleteFollow(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestParam followerId: Long,
        @RequestParam creatorId: Long,
    ) {
        followService.deleteFollow(userDetails, followerId, creatorId)
    }

}