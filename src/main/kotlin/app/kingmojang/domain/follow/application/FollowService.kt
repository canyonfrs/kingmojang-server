package app.kingmojang.domain.follow.application

import app.kingmojang.domain.follow.domain.Follow
import app.kingmojang.domain.follow.exception.FollowAlreadyExistException
import app.kingmojang.domain.follow.exception.NotFoundFollowException
import app.kingmojang.domain.follow.repository.FollowRepository
import app.kingmojang.domain.member.exception.NotFoundMemberException
import app.kingmojang.domain.member.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FollowService(
    private val memberRepository: MemberRepository,
    private val followRepository: FollowRepository,
) {

    @Transactional
    fun createFollow(userDetails: UserDetails, followerId: Long, creatorId: Long): Long {
        if (followRepository.existsByCreatorIdAndFollowerId(creatorId, followerId)) {
            throw FollowAlreadyExistException(followerId, creatorId)
        }
        val follower = memberRepository.findByIdOrNull(followerId) ?: throw NotFoundMemberException(followerId)
        val creator = memberRepository.findByIdOrNull(creatorId) ?: throw NotFoundMemberException(creatorId)
        return followRepository.save(Follow.create(creator, follower)).id!!
    }

    @Transactional
    fun deleteFollow(userDetails: UserDetails, followerId: Long, creatorId: Long) {
        val follow = (followRepository.findByCreatorIdAndFollowerId(creatorId, followerId)
            ?: throw NotFoundFollowException(followerId, creatorId))
        follow.remove()
        followRepository.delete(follow)
    }
}