package app.kingmojang.global.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.cors")
data class CorsProperty(
    val allowedOrigins: List<String>,
)