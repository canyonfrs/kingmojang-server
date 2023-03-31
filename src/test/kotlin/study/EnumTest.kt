package study

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class EnumTest {
    @Test
    fun `toString Test`() {
        Assertions.assertThat(HelloEnum.A.toString()).isEqualTo("A")
        Assertions.assertThat(HelloEnum.A.name).isEqualTo("A")
        Assertions.assertThat(HelloEnum.A.value).isEqualTo("a")
    }

    @Test
    fun `valueOf Test`() {
        Assertions.assertThat(HelloEnum.valueOf("a".uppercase())).isEqualTo(HelloEnum.A)
    }
}
enum class HelloEnum(val value: String) {
    A("a"), B("b")
}