package app.kingmojang.global.util

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.util.SerializationUtils
import java.util.*

class CookieUtils {
    companion object {
        fun getCookie(request: HttpServletRequest, name: String): Cookie? {
            return request.cookies.firstOrNull { it.name == name }
        }

        fun addCookie(response: HttpServletResponse, name: String, value: String, maxAge: Int) {
            val cookie = Cookie(name, value)
            cookie.path = "/"
            cookie.isHttpOnly = true
            cookie.maxAge = maxAge
            response.addCookie(cookie)
        }

        fun deleteCookie(request: HttpServletRequest, response: HttpServletResponse, name: String) {
            request.cookies.filter { it.name == name }.map {
                it.value = ""
                it.path = "/"
                it.maxAge = 0
                response.addCookie(it)
            }
        }

        fun serialize(o: Any): String? {
            return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(o))
        }

        fun <T> deserialize(cookie: Cookie, cls: Class<T>): T {
            return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.value)))
        }
    }
}