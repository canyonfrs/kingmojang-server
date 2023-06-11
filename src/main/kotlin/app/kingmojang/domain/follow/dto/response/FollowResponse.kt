package app.kingmojang.domain.follow.dto.response

import app.kingmojang.domain.follow.domain.Follow
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class FollowResponse @QueryProjection constructor(
    val profileImage: String,
    val nickname: String,
    val introduce: String?,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(follow: Follow): FollowResponse {
            return FollowResponse(
                follow.creator.profileImage,
                follow.creator.nickname,
                follow.creator.creatorInformation?.introduce,
                follow.createdAt
            )
        }
    }
}
