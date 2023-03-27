package study

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ObjectMapperTest{
    private val objectMapper: ObjectMapper = ObjectMapper()
    private val str = "{\"key1\":[1,2,3,4],\"key2\":{\"1\":2,\"3\":4,\"5\":6}}"
    @Test
    fun `map 여러개 테스트`() {
        val readValue = objectMapper.readValue<MutableMap<String, Any>>(str)
        Assertions.assertThat(readValue["key1"]).isNotNull
        println("[invalid_token_response]%20An%20error%20occurred%20while%20attempting%20to%20retrieve%20the%20OAuth%202.0%20Access%20Token%20Response:%20Error%20while%20extracting%20response%20for%20type%20[class%20org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse]%20and%20content%20type%20[application/json;charset=utf-8]".replace("%20", " "))

    }

}