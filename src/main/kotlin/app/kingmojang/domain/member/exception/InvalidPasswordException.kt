package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class InvalidPasswordException(
    override val message: String = "Invalid Password",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.BAD_CREDENTIAL_EXCEPTION, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}
