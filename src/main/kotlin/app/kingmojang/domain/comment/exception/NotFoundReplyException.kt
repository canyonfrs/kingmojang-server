package app.kingmojang.domain.comment.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundReplyException(
    private val id: Long,
    override val message: String = "Reply with replyId('$id') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_REPLY, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(id.toString())
}
