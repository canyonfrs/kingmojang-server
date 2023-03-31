package app.kingmojang.global.validator

import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes

class MemberIdValidator {
    companion object {
        fun validate(tokenMemberId: Long, memberId: Long) {
            if (tokenMemberId != memberId) throw CommonException(ErrorCodes.INVALID_JWT_TOKEN)
        }
    }
}