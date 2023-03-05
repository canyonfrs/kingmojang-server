package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class SameEmailAlreadyExistException(
    private val email: String,
    override val message: String = "User with email('$email') already exists.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.DUPLICATE_EMAIL, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(email)
}