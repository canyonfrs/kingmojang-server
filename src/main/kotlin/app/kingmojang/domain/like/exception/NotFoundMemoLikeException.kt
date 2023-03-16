package app.kingmojang.domain.like.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundMemoLikeException(
    private val id: Long,
    private val username: String,
    override val message: String = "MemoLike with memoId('$id'), username('$username') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_LIKE, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(id.toString())
}