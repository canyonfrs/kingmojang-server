package app.kingmojang.global.exception.common

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class BadRequestException(
    private val value: Any,
    override val message: String = "'$value' is bad request.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.BAD_REQUEST, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}