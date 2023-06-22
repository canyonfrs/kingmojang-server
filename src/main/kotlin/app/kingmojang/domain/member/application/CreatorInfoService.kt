package app.kingmojang.domain.member.application

import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.domain.CreatorInformation
import app.kingmojang.domain.member.dto.request.CreatorInformationRequest
import app.kingmojang.domain.member.dto.response.CreatorInfoResponse
import app.kingmojang.domain.member.exception.NotFoundCreatorInfoException
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.CreatorInfoRepository
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.exception.common.BadRequestException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreatorInfoService(
    private val memberRepository: MemberRepository,
    private val creatorInfoRepository: CreatorInfoRepository,
    private val followRepository: FollowRepository,
) {

    fun createCreatorInfo(memberId: Long, request: CreatorInformationRequest): Long {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val creatorInfo = creatorInfoRepository.save(CreatorInformation.create(request))
        member.createCreatorInformation(creatorInfo)
        return creatorInfo.id!!
    }

    fun updateCreatorInfo(memberId: Long, request: CreatorInformationRequest): Long {
        val member = memberRepository.findByIdOrNull(memberId) ?: throw NotFoundMemberException(memberId)
        val memberCreatorInfo = member.creatorInformation ?: throw NotFoundCreatorInfoException()
        return memberCreatorInfo.update(request).id!!
    }

    @Transactional(readOnly = true)
    fun readCreatorInformation(memberId: Long?, creatorId: Long): CreatorInfoResponse {
        val creator = memberRepository.findByIdOrNull(creatorId) ?: throw NotFoundMemberException(creatorId)
        if (creator.isCreator().not()) {
            throw BadRequestException("The member is not a creator.")
        }
        val creatorInformation = creator.creatorInformation ?: throw NotFoundCreatorInfoException()
        val isFollow =
            if (memberId != null) followRepository.existsByCreatorIdAndFollowerId(creatorId, memberId) else false
        return CreatorInfoResponse.of(creatorInformation, isFollow)
    }

}