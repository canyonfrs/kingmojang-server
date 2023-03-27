package app.kingmojang.global.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.oauth2")
data class OAuth2Property(
    val authorizedRedirectUris: List<String>,
)