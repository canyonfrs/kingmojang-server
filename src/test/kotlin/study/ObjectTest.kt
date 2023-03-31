package study

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class ObjectTest {

    @Test
    fun `object test`() {
        val map = mutableMapOf<String, Int>()
        test1(map)
        Assertions.assertThat(map.containsKey("key")).isTrue
    }

    private fun test1(map: MutableMap<String, Int>): String {
        map["key"] = 1
        return "key"
    }
}