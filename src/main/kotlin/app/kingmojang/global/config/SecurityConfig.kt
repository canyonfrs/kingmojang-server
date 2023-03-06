package app.kingmojang.global.config

import app.kingmojang.global.security.JwtAuthFilter
import app.kingmojang.global.security.JwtExceptionFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val jwtExceptionFilter: JwtExceptionFilter,
    private val authenticationProvider: AuthenticationProvider,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin().disable()
        http {
            cors { disable() }
            csrf { disable() }
            httpBasic { disable() }
            sessionManagement { SessionCreationPolicy.STATELESS }
        }
        http.authorizeHttpRequests {
            it.requestMatchers("/api/v1/**").permitAll()
                .anyRequest().authenticated()
        }
        http.authenticationProvider(authenticationProvider)
        http.addFilter(CorsFilter(corsConfigurationSource()))
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(jwtExceptionFilter, JwtAuthFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        // TODO update details settings
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}