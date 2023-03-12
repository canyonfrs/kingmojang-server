package study

import org.junit.jupiter.api.Test

class StringTest {
    companion object {
        private const val SUMMARY_DEFAULT_SIZE = 160
    }

    @Test
    fun `drop 테스트`() {
        val str: String = """0123456789012345678901234567890
            |0123456789012345678901234567890
            |0123456789012345678901234567890
            |0123456789012345678901234567890
            |0123456789012345678901234567890
        """.trimMargin()

        println(str.length)
        println("==========================")
        println(str.substring(0, if (str.length > SUMMARY_DEFAULT_SIZE) SUMMARY_DEFAULT_SIZE else str.length))

    }
}