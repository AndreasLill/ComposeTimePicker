package com.andlill.timepicker

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@Composable
internal fun TimePickerClockDial(
    timeSelected: LocalTime,
    timeUnit: TimeUnit,
    is24h: Boolean,
    onSelectTime: (LocalTime) -> Unit
) {
    Surface(
        modifier = Modifier.size(256.dp),
        shape = RoundedCornerShape(256.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        content = {
            Box(Modifier.fillMaxSize()) {
                TimePickerCanvas(
                    timeSelected = timeSelected,
                    timeUnit = timeUnit,
                    is24h = is24h,
                    selectionColor = MaterialTheme.colorScheme.primary,
                    selectionTextColor = MaterialTheme.colorScheme.onPrimary,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    onSelectTime = onSelectTime
                )
            }
        }
    )
}