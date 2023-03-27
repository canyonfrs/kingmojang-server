package app.kingmojang.global.property

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "app.auth")
data class AuthProperty(
    val secretKey: String,
    val expTime: Long,
)