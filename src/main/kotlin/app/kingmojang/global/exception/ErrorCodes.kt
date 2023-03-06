package app.kingmojang.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCodes(val status: HttpStatus, val description: String) {
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, ""),
    DUPLICATE_NICKNAME(HttpStatus.CONFLICT, ""),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, ""),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, ""),
    NOT_FOUND_USERNAME(HttpStatus.NOT_FOUND, ""),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, ""),
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "토큰의 시그니처가 잘못되었습니다."),
    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰 입니다."),
    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "Access 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh 토큰이 만료되었습니다.")
}