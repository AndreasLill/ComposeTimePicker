package com.andlill.timepicker

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

fun LocalTime.toString(pattern: String, locale: Locale): String {
    val formatter = DateTimeFormatter.ofPattern(pattern).withLocale(locale)
    return this.format(formatter)
}