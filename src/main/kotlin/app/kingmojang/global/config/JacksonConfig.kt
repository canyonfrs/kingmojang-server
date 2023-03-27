package app.kingmojang.global.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Configuration
class JacksonConfig(
    private val defaultObjectMapper: ObjectMapper,
) : InitializingBean {
    override fun afterPropertiesSet() {
        val simpleModule = SimpleModule().apply {
            addSerializer(LocalDateTime::class.java, CustomLocalDateTimeSerializer())
        }
        defaultObjectMapper.registerModules(simpleModule).registerKotlinModule()
    }
}

internal class CustomLocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.atZone(ZoneId.systemDefault())?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}