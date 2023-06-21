package app.kingmojang.domain.follow.dto.response

import app.kingmojang.domain.follow.domain.Follow
import org.springframework.data.domain.Slice

data class FollowsResponse(
    val follows: List<FollowResponse>,
    val hasNext: Boolean,
    val nextPage: Int,
) {
    companion object {
        fun of(follows: Slice<Follow>): FollowsResponse {
            return FollowsResponse(
                follows.content.map { FollowResponse.of(it) },
                follows.hasNext(),
                if (follows.hasNext()) follows.nextPageable().pageNumber else -1
            )
        }
    }
}