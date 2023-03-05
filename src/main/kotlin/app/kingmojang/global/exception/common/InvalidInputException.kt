package app.kingmojang.global.exception.common

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class InvalidInputException(
    private val value: Any,
    override val message: String = "'$value' is not a valid input value.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.INVALID_INPUT, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}