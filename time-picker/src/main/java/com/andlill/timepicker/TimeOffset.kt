package com.andlill.timepicker

import androidx.compose.ui.geometry.Offset
import java.time.LocalTime

data class TimeOffset(
    val time: LocalTime,
    val offset: Offset
) {
    companion object {
        val Unspecified = TimeOffset(LocalTime.of(0, 0), Offset(0f, 0f))
    }
}
