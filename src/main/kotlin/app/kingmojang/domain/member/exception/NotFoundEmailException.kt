package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundEmailException(
    private val email: String,
    override val message: String = "User with email('$email') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_USERNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(email)
}