package com.github.kimleepark2.common

import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

open class BaseCondition(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var startDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var endDate: LocalDate? = null,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    val startDateTime: LocalDateTime
        get() {
            if (startDate == null) {
                startDate = LocalDate.of(1970, 1, 1)
            }
            return LocalDateTime.of(startDate, LocalTime.of(0, 0, 0))
        }

    val endDateTime: LocalDateTime
        get() {
            if (endDate == null) {
                endDate = LocalDate.now().plusDays(1)
            }
            return LocalDateTime.of(endDate, LocalTime.of(23, 59, 59))
        }

    val isDateRangeValid: Boolean
        get() {
            return startDate != null || endDate != null
        }
}