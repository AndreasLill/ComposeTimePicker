package com.andlill.timepicker

import java.time.LocalTime
import java.time.format.DateTimeFormatter

internal fun LocalTime.toString(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}