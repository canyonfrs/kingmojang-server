package app.kingmojang.global.security

import app.kingmojang.global.common.response.CommonResponse
import app.kingmojang.global.common.response.ExceptionBody
import app.kingmojang.global.exception.CommonException
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: CommonException) {
            val errorCode = e.codeBook
            response.status = errorCode.status.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.characterEncoding = "UTF-8"
            objectMapper.writeValue(response.writer, CommonResponse.error(errorCode.name, ExceptionBody.of(errorCode)))
        }
    }
}