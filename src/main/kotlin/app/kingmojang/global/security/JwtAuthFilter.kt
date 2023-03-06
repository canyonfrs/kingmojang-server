package app.kingmojang.global.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtUtils: JwtUtils,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {

    companion object {
        const val TOKEN_TYPE = "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return filterChain.doFilter(request, response)
        }
        val token = authHeader.substring(TOKEN_TYPE.length)
        if (jwtUtils.validation(token)) {
            val username = jwtUtils.extractUsername(token)
            val context = SecurityContextHolder.createEmptyContext()
            val userDetails = userDetailsService.loadUserByUsername(username)
            val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            context.authentication = authToken
            SecurityContextHolder.setContext(context)
        }
        filterChain.doFilter(request, response)
    }
}