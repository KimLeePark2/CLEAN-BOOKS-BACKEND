package com.github.kimleepark2.common

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

open class BaseCondition(
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    private var startDate: LocalDate? = null,

    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    private var endDate: LocalDate? = null,
) {

    val startDateTime: LocalDateTime
        get() {
            if (startDate == null) {
                startDate = LocalDate.now().minusMonths(1)
            }
            return LocalDateTime.of(startDate, LocalTime.of(0, 0, 0))
        }

    val endDateTime: LocalDateTime
        get() {
            if (endDate == null) {
                endDate = LocalDate.now().plusDays(1)
            }
            return LocalDateTime.of(endDate, LocalTime.of(0, 0, 0))
        }
}