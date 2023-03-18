package app.kingmojang.domain.follow.repository

import app.kingmojang.domain.follow.domain.Follow
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow, Long> {
    fun existsByCreatorIdAndFollowerId(creatorId: Long, followerId: Long): Boolean
    fun findByCreatorIdAndFollowerId(creatorId: Long, followerId: Long): Follow?
}