package app.kingmojang.domain.follow.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class FollowAlreadyExistException(
    private val followerId: Long,
    private val creatorId: Long,
    override val message: String = "Follow with followerId('$followerId'), creatorId('$creatorId') already exists.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.DUPLICATE_FOLLOW, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}