package app.kingmojang.domain.member.exception

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class SameNicknameAlreadyExistException(
    private val nickname: String,
    override val message: String = "User with nickname('$nickname') already exists.",
    override val cause: Throwable? = null,
) : CommonException(ErrorCodes.DUPLICATE_NICKNAME, message, cause) {
    override fun messageArguments(): Collection<String> = setOf(nickname)
}