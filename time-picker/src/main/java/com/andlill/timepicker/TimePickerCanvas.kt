package com.andlill.timepicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun TimePickerCanvas(
    timeSelected: LocalTime,
    timeUnit: TimeUnit,
    is24h: Boolean,
    selectionColor: Color,
    selectionTextColor: Color,
    textColor: Color,
    onSelectTime: (LocalTime) -> Unit
) {
    val hours = remember { mutableListOf<TimeOffset>() }
    var selected by remember { mutableStateOf(TimeOffset.Unspecified) }
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .size(256.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        change.consume()
                        selected = hours.sortedBy { hour ->
                            (change.position.x - hour.offset.x).pow(2) + (change.position.y - hour.offset.y).pow(2)
                        }.first()
                        onSelectTime(timeSelected.withHour(selected.time.hour))
                    },
                    onDragEnd = {
                        // TODO: move to minutes
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures { press ->
                    // Get the closest position to the press.
                    selected = hours.sortedBy { hour ->
                        (press.x - hour.offset.x).pow(2) + (press.y - hour.offset.y).pow(2)
                    }.first()
                    onSelectTime(timeSelected.withHour(selected.time.hour))
                    // TODO: move to minutes
                }
            },
        onDraw = {
            val padding = 4.dp.toPx()
            val drawRadius = 22.dp.toPx()
            val clockRadius = size.height / 2

            if (hours.isEmpty()) {
                if (is24h) {
                    (0..23).forEach { hour ->
                        hours.add(
                            TimeOffset(
                                time = LocalTime.of(hour, 0),
                                offset = Offset(
                                    x = size.width / 2 + cos((hour-6) * 360/24 * PI /180).toFloat() * (clockRadius - drawRadius - padding),
                                    y = size.height / 2 + sin((hour-6) * 360/24 * PI /180).toFloat() * (clockRadius - drawRadius - padding)
                                )
                            )
                        )
                    }
                }
                else {
                    (0..11).forEach { hour ->
                        hours.add(
                            TimeOffset(
                                time = LocalTime.of(hour, 0),
                                offset = Offset(
                                    x = size.width / 2 + cos((hour-3) * 360/12 * PI /180).toFloat() * (clockRadius - drawRadius - padding),
                                    y = size.height / 2 + sin((hour-3) * 360/12 * PI /180).toFloat() * (clockRadius - drawRadius - padding)
                                )
                            )
                        )
                    }
                }
            }

            if (selected == TimeOffset.Unspecified) {
                hours.find { it.time.hour == timeSelected.hour }?.let {
                    selected = it
                }
            }

            drawCircle(
                color = selectionColor,
                radius = drawRadius,
                center = selected.offset
            )

            hours.forEachIndexed { index, timeOffset ->
                val str = "$index"
                val textSize = textMeasurer.measure(text = AnnotatedString(str))
                val draw = (is24h && index % 2 == 0) || (!is24h)

                if (draw) {
                    drawText(
                        textMeasurer = textMeasurer,
                        text = str,
                        style = TextStyle(
                            color = if (timeOffset == selected) selectionTextColor else textColor,
                            fontSize = 14.sp
                        ),
                        topLeft = Offset(
                            x = timeOffset.offset.x - (textSize.size.width / 2),
                            y = timeOffset.offset.y - (textSize.size.height / 2)
                        )
                    )
                }
            }
        }
    )
}