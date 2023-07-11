package app.kingmojang.global.common.request

import org.springframework.data.domain.Sort
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

data class NoOffsetPageRequest(val updateAt: LocalDateTime, val size: Int, val sort: Sort) {
    companion object {
        private fun convert(timestamp: Long?): LocalDateTime = if (timestamp == null) {
            LocalDateTime.MAX
        } else {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), TimeZone.getDefault().toZoneId())
        }

        fun of(timestamp: Long?, size: Int): NoOffsetPageRequest {
            return NoOffsetPageRequest(convert(timestamp), size, Sort.unsorted())
        }

        fun of(timestamp: Long?, size: Int, sort: Sort): NoOffsetPageRequest {
            return NoOffsetPageRequest(convert(timestamp), size, sort)
        }
    }

    fun isSorted(): Boolean = sort.isSorted
}