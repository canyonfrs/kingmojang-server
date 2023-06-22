package app.kingmojang.global.exception.common

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class InvalidMemberException(
    private val value: Any,
    override val message: String = "'$value' is invalid member.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.INVALID_JWT_TOKEN, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}