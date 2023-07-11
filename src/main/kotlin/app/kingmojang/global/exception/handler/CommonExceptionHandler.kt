package app.kingmojang.global.exception.handler

import app.kingmojang.global.common.response.CommonResponse
import app.kingmojang.global.common.response.ExceptionBody
import app.kingmojang.global.exception.CommonException
import app.kingmojang.global.exception.ErrorCodes
import app.kingmojang.global.exception.common.InvalidInputException
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class CommonExceptionHandler {
    @ExceptionHandler(CommonException::class)
    fun onCommonException(e: CommonException): ResponseEntity<CommonResponse<ExceptionBody>> {
        return postProcessError(e)
    }

    @ExceptionHandler(BindException::class)
    fun onBindException(e: BindException): ResponseEntity<CommonResponse<ExceptionBody>> {
        return postProcessError(
            InvalidInputException(
                value = e.fieldErrors.map { it.field }.toSortedSet().joinToString { it },
                cause = e
            )
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun onBindException(e: BadCredentialsException): ResponseEntity<CommonResponse<ExceptionBody>> {
        return ResponseEntity.status(401)
            .body(CommonResponse.error(e.message!!, ExceptionBody.of(ErrorCodes.BAD_CREDENTIAL_EXCEPTION)))
    }

    @ExceptionHandler(Exception::class)
    fun onException(e: Exception): ResponseEntity<CommonResponse<ExceptionBody>> {
        logger.error { "Exception: ${e.message}" }
        return postProcessError(
            CommonException(ErrorCodes.UNHANDLED_EXCEPTION, DEFAULT_ERROR_MESSAGE, e)
        )
    }

    private fun postProcessError(
        exception: CommonException,
    ): ResponseEntity<CommonResponse<ExceptionBody>> {
        val message = exception.message
        val codeBook = exception.codeBook
        val response = CommonResponse.error(message, ExceptionBody.of(codeBook))

        return ResponseEntity(response, codeBook.status)
    }

    companion object {
        const val DEFAULT_ERROR_MESSAGE = "Cannot process this request."
    }
}