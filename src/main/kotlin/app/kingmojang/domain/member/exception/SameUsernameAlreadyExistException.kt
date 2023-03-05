package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class SameUsernameAlreadyExistException(
    private val username: String,
    override val message: String = "User with username('$username') already exists.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.DUPLICATE_USERNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(username)
}