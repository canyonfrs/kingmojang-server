package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotFoundCreatorInfoException(
    private val creatorInfoId: Long = -1L,
    override val message: String = "CreatorInformation with creatorInfoId('$creatorInfoId') not found.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.NOT_FOUND_USERNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}
