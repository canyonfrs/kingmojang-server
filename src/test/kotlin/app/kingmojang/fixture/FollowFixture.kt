package app.kingmojang.fixture

import app.kingmojang.domain.follow.domain.Follow
import app.kingmojang.domain.member.domain.Member
import java.time.LocalDateTime

const val CREATOR_ID = 1L
const val FOLLOWER_ID = 2L
const val FOLLOW_ID = 1L
val FOLLOW_CREATED_AT: LocalDateTime = LocalDateTime.of(2023, 5, 10, 12, 12, 11)

fun createFollow(
    followId: Long = FOLLOW_ID,
    creator: Member = createMember(memberId = CREATOR_ID),
    follower: Member = createMember(memberId = FOLLOWER_ID),
    createdAt: LocalDateTime = FOLLOW_CREATED_AT
): Follow {
    return Follow(followId, creator, follower, createdAt)
}