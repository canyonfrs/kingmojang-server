package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundUsernameException(
    private val username: String,
    override val message: String = "User with username('$username') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_USERNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(username)
}