package app.kingmojang.global.exception.common

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotWriterException(
    private val value: Any,
    override val message: String = "'$value' is not writer.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.INVALID_MEMBER_AUTHORITY, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}