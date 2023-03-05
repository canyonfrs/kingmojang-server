package app.kingmojang.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCodes(val status: HttpStatus, val description: String) {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, ""),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, ""),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, ""),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, ""),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "")
}