package app.kingmojang.domain.authenticationcode.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class UnauthorizedCreateAuthCodeException(
    private val memberId: Long,
    override val message: String = "User with memberId('$memberId') unauthorized.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_USERNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}
