package study

import org.junit.jupiter.api.Test
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*


class TimeConvertTest {

    @Test
    fun `LocalDateTime Timestamp 변환하기`() {
        val now = LocalDateTime.now()
        println("LocalDateTime : $now")
        println("LocalDateTime to Long value : ${now.toEpochSecond(ZoneOffset.of("+9"))}")
        println("LocalDateTime to Timestamp : ${Timestamp.valueOf(now).time}")

        // Timestamp to LocalDateTime
        val systemLocalDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(Timestamp.valueOf(now).time), TimeZone.getDefault().toZoneId())
        println(systemLocalDateTime)
    }


}