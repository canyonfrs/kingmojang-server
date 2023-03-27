package study

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class CollectionsTest {
    val list = listOf("ABC", "DEF", "GHI")

    @Test
    fun `any test1`() {
        Assertions.assertThat(list.any {
            (it == "ABC")
        }).isTrue

        Assertions.assertThat(list.any {
            (it == "GHI")
        }).isTrue
    }

    @Test
    fun `any test2`() {
        Assertions.assertThat(list.any {
            (it == "NONE")
        }).isFalse
    }
}