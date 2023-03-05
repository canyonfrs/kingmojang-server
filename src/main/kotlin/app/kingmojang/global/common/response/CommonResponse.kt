package app.kingmojang.global.common.response

import app.kingmojang.global.common.response.ResponseStatus.*
import app.kingmojang.global.exception.ErrorCodes
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class CommonResponse<T>(
    val status: ResponseStatus,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val message: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val data: T?,
) {
    companion object {
        fun success(): CommonResponse<Void> {
            return CommonResponse(SUCCESS, null, null)
        }

        fun <T> success(data: T): CommonResponse<T> {
            return CommonResponse(SUCCESS, null, data)
        }

        fun <T> fail(data: T): CommonResponse<T> {
            return CommonResponse(FAIL, null, data)
        }

        fun error(message: String, data: ExceptionBody): CommonResponse<ExceptionBody> {
            return CommonResponse(ERROR, message, data)
        }
    }
}

data class ExceptionBody(
    val name: String,
    val status: HttpStatus,
    val timestamp: LocalDateTime = LocalDateTime.now(),
) {
    companion object {
        fun of(code: ErrorCodes): ExceptionBody {
            return ExceptionBody(code.name, code.status)
        }
    }
}