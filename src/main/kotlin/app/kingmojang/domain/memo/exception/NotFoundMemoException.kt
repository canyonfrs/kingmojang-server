package app.kingmojang.domain.memo.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundMemoException(
    private val id: Long,
    override val message: String = "Memo with memoId('$id') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_MEMO, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(id.toString())
}