package app.kingmojang.domain.member.application

import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.domain.CreatorInformation
import app.kingmojang.domain.member.domain.Member
import app.kingmojang.domain.member.domain.MemberType
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.dto.request.CreatorInformationRequest
import app.kingmojang.domain.member.dto.response.CreatorInfoResponse
import app.kingmojang.domain.member.exception.NotFoundCreatorInfoException
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.CreatorInfoRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.common.BadRequestException
import app.kingmojang.global.validator.MemberIdValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreatorInfoService(
    private val memberRepository: MemberRepository,
    private val creatorInfoRepository: CreatorInfoRepository,
    private val followRepository: FollowRepository,
) {

    @Transactional
    fun createCreatorInfo(
        userPrincipal: UserPrincipal,
        memberId: Long,
        request: CreatorInformationRequest,
    ): Long {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val creatorInfo = creatorInfoRepository.save(CreatorInformation.create(request))
        member.createCreatorInformation(creatorInfo)
        return creatorInfo.id!!
    }

    @Transactional
    fun updateCreatorInfo(
        userPrincipal: UserPrincipal,
        memberId: Long,
        request: CreatorInformationRequest,
    ): Long {
        MemberIdValidator.validate(userPrincipal.getId(), memberId)
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memberCreatorInfo = member.creatorInformation ?: throw NotFoundCreatorInfoException()
        return memberCreatorInfo.update(request).id!!
    }

    @Transactional(readOnly = true)
    fun readCreatorInformation(
        userPrincipal: UserPrincipal?,
        creatorId: Long,
    ): CreatorInfoResponse {
        val creator = memberRepository.findByIdOrNull(creatorId) ?: throw NotFoundMemberException(creatorId)
        if (isNotCreator(creator)) {
            throw BadRequestException("The member is not a creator.")
        }
        val creatorInformation = creator.creatorInformation ?: throw NotFoundCreatorInfoException()
        if (userPrincipal == null) {
            return CreatorInfoResponse.of(creatorInformation, false)
        }
        val memberId = userPrincipal.getId()
        val isFollow = followRepository.existsByCreatorIdAndFollowerId(creatorId, memberId)
        return CreatorInfoResponse.of(creatorInformation, isFollow)
    }

    private fun isNotCreator(creator: Member) = creator.type != MemberType.CREATOR
}