package app.kingmojang.domain.follow.application

import app.kingmojang.domain.follow.domain.Follow
import app.kingmojang.domain.follow.dto.response.FollowResponse
import app.kingmojang.domain.follow.dto.response.FollowsResponse
import app.kingmojang.domain.follow.exception.FollowAlreadyExistException
import app.kingmojang.domain.follow.exception.NotFoundFollowException
import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.domain.UserPrincipal
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import app.kingmojang.global.common.request.CommonPageRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FollowService(
    private val memberRepository: MemberRepository,
    private val followRepository: FollowRepository,
) {

    fun createFollow(followerId: Long, creatorId: Long): Long {
        if (followRepository.existsByCreatorIdAndFollowerId(creatorId, followerId)) {
            throw FollowAlreadyExistException(followerId, creatorId)
        }
        val follower = memberRepository.findByIdOrNull(followerId) ?: throw NotFoundMemberException(followerId)
        val creator = memberRepository.findByIdOrNull(creatorId) ?: throw NotFoundMemberException(creatorId)
        return followRepository.save(Follow.create(creator, follower)).id!!
    }

    fun deleteFollow(followerId: Long, creatorId: Long) {
        val follow = (followRepository.findByCreatorIdAndFollowerId(creatorId, followerId)
            ?: throw NotFoundFollowException(followerId, creatorId))
        follow.remove()
        followRepository.delete(follow)
    }

    @Transactional(readOnly = true)
    fun readFollowByFollower(memberId: Long, request: PageRequest): FollowsResponse {
        val follows = followRepository.findAllByFollowerId(memberId, request.withSort(Sort.by("createdAt").descending()))
        return FollowsResponse.of(follows)
    }
}