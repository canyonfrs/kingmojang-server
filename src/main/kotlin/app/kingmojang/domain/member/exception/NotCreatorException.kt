package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class NotCreatorException(
    private val memberId: Long = -1L,
    override val message: String = "Member with memberId('$memberId') is not a creator.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.INVALID_MEMBER_TYPE, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(message)
}
