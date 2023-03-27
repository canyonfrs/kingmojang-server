package app.kingmojang.global.config

import app.kingmojang.global.property.AuthProperty
import app.kingmojang.global.property.CorsProperty
import app.kingmojang.global.property.OAuth2Property
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        AuthProperty::class,
        OAuth2Property::class,
        CorsProperty::class
    ]
)
class PropertyConfig {
}