package app.kingmojang.domain.follow.repository

import app.kingmojang.domain.follow.domain.Follow
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface FollowRepository : JpaRepository<Follow, Long> {
    fun existsByCreatorIdAndFollowerId(creatorId: Long, followerId: Long): Boolean
    fun findByCreatorIdAndFollowerId(creatorId: Long, followerId: Long): Follow?

    @EntityGraph(attributePaths = ["creator"])
    fun findAllByFollowerId(followerId: Long, page: Pageable): Slice<Follow>

    @EntityGraph(attributePaths = ["creator"])
    fun findAllByFollowerId(followerId: Long, sort: Sort): List<Follow>
}