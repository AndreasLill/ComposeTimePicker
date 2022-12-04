package com.andlill.timepicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
internal fun TimePickerClockDial() {
    Surface(
        modifier = Modifier.size(256.dp),
        shape = RoundedCornerShape(256.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        content = {
            Box(Modifier.fillMaxSize()) {
                TimePickerCanvas()
            }
        }
    )
}

@Composable
internal fun TimePickerCanvas() {
    var position by remember { mutableStateOf(Offset.Unspecified) }
    Canvas(
        modifier = Modifier
            .size(256.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    position += dragAmount
                }
            }
            .pointerInput(Unit) {
                detectTapGestures {
                    position = it
                }
            },
        onDraw = {
            val radius = size.height / 2

            if (position == Offset.Unspecified)
                position = Offset(size.width - radius, size.height / 2)

            drawCircle(
                color = Color.Blue,
                radius = 48f,
                center = position
            )
        }
    )
}