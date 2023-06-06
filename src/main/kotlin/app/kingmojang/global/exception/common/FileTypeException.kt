package app.kingmojang.global.exception.common

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class FileTypeException(
    private val value: Any,
    override val message: String = "'$value' is not a valid image type.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.INVALID_FILE_TYPE, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(value.toString())
}