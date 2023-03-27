package app.kingmojang.global.config

import app.kingmojang.global.property.CorsProperty
import app.kingmojang.global.security.JwtAuthFilter
import app.kingmojang.global.security.JwtExceptionFilter
import app.kingmojang.global.security.oauth2.CustomOAuth2UserService
import app.kingmojang.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import app.kingmojang.global.security.oauth2.OAuth2AuthenticationFailureHandler
import app.kingmojang.global.security.oauth2.OAuth2AuthenticationSuccessHandler
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
    private val httpCookieOAuth2AuthorizationRequestRepository: HttpCookieOAuth2AuthorizationRequestRepository,
    private val customOAuth2UserService: CustomOAuth2UserService,
    private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val corsProperty: CorsProperty,
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
                .requestMatchers("/oauth2/**").permitAll()
                .anyRequest().authenticated()
        }
        http {
            oauth2Login {
                authorizationEndpoint {
                    baseUri = "/oauth2/authorize"
                    authorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository
                }
                redirectionEndpoint {
                    baseUri = "/*/oauth2/code/*"
                }
                userInfoEndpoint {
                    userService = customOAuth2UserService
                }
                authenticationSuccessHandler = oAuth2AuthenticationSuccessHandler
                authenticationFailureHandler = oAuth2AuthenticationFailureHandler
            }
        }
        http.authenticationProvider(authenticationProvider)
        http.addFilter(CorsFilter(corsConfigurationSource()))
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(jwtExceptionFilter, JwtAuthFilter::class.java)

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = corsProperty.allowedOrigins
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}