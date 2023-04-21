package app.kingmojang.domain.authenticationcode.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class ExpiredAuthCodeException(
    private val authCode: Int,
    override val message: String = "Expired authCode('$authCode')",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.EXPIRED_AUTH_CODE, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}