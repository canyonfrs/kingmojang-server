package app.kingmojang.domain.authenticationcode.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundAuthCodeException(
    private val authCode: Int,
    override val message: String = "AuthCode('$authCode') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_AUTH_CODE, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}