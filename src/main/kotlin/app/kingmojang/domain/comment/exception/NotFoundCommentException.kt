package app.kingmojang.domain.comment.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundCommentException(
    private val id: Long,
    override val message: String = "Comment with commentId('$id') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_COMMENT, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(id.toString())
}
