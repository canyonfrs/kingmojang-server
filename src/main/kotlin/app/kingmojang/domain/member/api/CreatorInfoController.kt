package app.kingmojang.domain.member.api

import app.kingmojang.domain.member.application.CreatorInfoService
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.dto.request.CreatorInformationRequest
import app.kingmojang.domain.member.dto.response.CreatorInfoResponse
import app.kingmojang.global.common.response.CommonResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.lang.IllegalStateException

@RestController
@RequestMapping("/api/v1/members")
class CreatorInfoController(
    private val creatorInfoService: CreatorInfoService,
) {

    @PostMapping("/{memberId}/creator-infos")
    @PreAuthorize("hasRole('CREATOR')")
    fun createCreatorInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
        @RequestBody @Valid request: CreatorInformationRequest,
    ): ResponseEntity<CommonResponse<Long>> {
          if (!userPrincipal.isValidMember(memberId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        val creatorInfoId = creatorInfoService.createCreatorInfo(memberId, request)

        val uri = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/members/$memberId/creator-infos/$creatorInfoId").buildAndExpand().toUri()

        return ResponseEntity.created(uri).body(CommonResponse.success(creatorInfoId))
    }

    @PutMapping("/{memberId}/creator-infos")
    @PreAuthorize("hasRole('CREATOR')")
    fun updateCreatorInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal,
        @PathVariable memberId: Long,
        @RequestBody @Valid request: CreatorInformationRequest,
    ): ResponseEntity<CommonResponse<Long>> {
        if (!userPrincipal.isValidMember(memberId)) {
            throw IllegalStateException("The member id is not equal to the user id.")
        }
        val creatorInfoId = creatorInfoService.updateCreatorInfo(memberId, request)
        return ResponseEntity.ok().body(CommonResponse.success(creatorInfoId))
    }

    @GetMapping("/{memberId}/creator-infos")
    fun readCreatorInformation(
        @AuthenticationPrincipal userPrincipal: UserPrincipal?,
        @PathVariable(value = "memberId") creatorId: Long,
    ): ResponseEntity<CommonResponse<CreatorInfoResponse>> {
        val memberId = userPrincipal?.getId()
        val creatorInfo = creatorInfoService.readCreatorInformation(memberId, creatorId)
        return ResponseEntity.ok().body(CommonResponse.success(creatorInfo))
    }
}