package com.andlill.timepicker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
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
internal fun TimePickerClock(
    timeSelected: LocalTime,
    timeUnit: TimeUnit,
    is24h: Boolean,
    onSelectTime: (LocalTime) -> Unit,
    onChangeTimeUnit: (TimeUnit) -> Unit
) {
    val selectionColor = MaterialTheme.colorScheme.primary
    val selectionTextColor = MaterialTheme.colorScheme.onPrimary
    val textColor = MaterialTheme.colorScheme.onSurface
    val hours = remember { mutableListOf<TimeOffset>() }
    val minutes = remember { mutableListOf<TimeOffset>() }
    var selected by remember(timeUnit) { mutableStateOf(TimeOffset.Unspecified) }
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = Modifier
            .size(256.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(256.dp)
            )
            .pointerInput(timeUnit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        change.consume()
                        if (timeUnit == TimeUnit.Hour) {
                            selected = hours
                                .sortedBy { hour ->
                                    (change.position.x - hour.offset.x).pow(2) + (change.position.y - hour.offset.y).pow(
                                        2
                                    )
                                }
                                .first()
                            onSelectTime(timeSelected.withHour(selected.time.hour))
                        } else {
                            selected = minutes
                                .sortedBy { minute ->
                                    (change.position.x - minute.offset.x).pow(2) + (change.position.y - minute.offset.y).pow(
                                        2
                                    )
                                }
                                .first()
                            onSelectTime(timeSelected.withMinute(selected.time.minute))
                        }
                    },
                    onDragEnd = {
                        if (timeUnit == TimeUnit.Hour) {
                            // TODO: Add animations.
                            onChangeTimeUnit(TimeUnit.Minute)
                        }
                    }
                )
            }
            .pointerInput(timeUnit) {
                detectTapGestures { press ->
                    // Get the closest position to the press.
                    if (timeUnit == TimeUnit.Hour) {
                        selected = hours
                            .sortedBy { hour ->
                                (press.x - hour.offset.x).pow(2) + (press.y - hour.offset.y).pow(2)
                            }
                            .first()
                        onSelectTime(timeSelected.withHour(selected.time.hour))
                        // TODO: Add animations.
                        onChangeTimeUnit(TimeUnit.Minute)
                    } else {
                        selected = minutes
                            .sortedBy { minute ->
                                (press.x - minute.offset.x).pow(2) + (press.y - minute.offset.y).pow(
                                    2
                                )
                            }
                            .first()
                        onSelectTime(timeSelected.withMinute(selected.time.minute))
                    }
                }
            },
        onDraw = {
            val padding = 4.dp.toPx()
            val selectedRadius = 22.dp.toPx()

            if (hours.isEmpty()) {
                addHours(
                    list = hours,
                    is24h = is24h,
                    canvasWidth = size.width,
                    canvasHeight = size.height,
                    selectedRadius = selectedRadius,
                    padding = padding
                )
            }
            if (minutes.isEmpty()) {
                addMinutes(
                    list = minutes,
                    canvasWidth = size.width,
                    canvasHeight = size.height,
                    selectedRadius = selectedRadius,
                    padding = padding
                )
            }
            if (selected == TimeOffset.Unspecified) {
                if (timeUnit == TimeUnit.Hour) {
                    hours.find { it.time.hour == timeSelected.hour }?.let {
                        selected = it
                    }
                }
                else {
                    minutes.find { it.time.minute == timeSelected.minute }?.let {
                        selected = it
                    }
                }
            }

            // Middle canvas circle.
            drawCircle(
                color = selectionColor,
                radius = 4.dp.toPx(),
                center = Offset(
                    x = size.width / 2,
                    y = size.height / 2
                )
            )
            // Line from middle circle to selection circle.
            drawLine(
                color = selectionColor,
                strokeWidth = 2.dp.toPx(),
                start = Offset(
                    x = size.width / 2,
                    y = size.height / 2
                ),
                end = Offset(
                    x = selected.offset.x,
                    y = selected.offset.y
                )
            )

            // Background circle to show behind blended circle to show as text color.
            drawCircle(
                color = selectionTextColor,
                radius = selectedRadius * 0.99f,
                center = selected.offset
            )

            drawIntoCanvas {
                with(drawContext.canvas.nativeCanvas) {
                    // Draw with layers to enable the blend mode.
                    val checkPoint = saveLayer(null, null)

                    if (timeUnit == TimeUnit.Hour) {
                        hours.forEachIndexed { index, timeOffset ->
                            val str = "$index"
                            val textSize = textMeasurer.measure(text = AnnotatedString(str))
                            if ((is24h && index % 2 == 0) || !is24h) {
                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = str,
                                    style = TextStyle(
                                        color = textColor,
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
                    else {
                        minutes.forEachIndexed { index, timeOffset ->
                            val str = "$index"
                            val textSize = textMeasurer.measure(text = AnnotatedString(str))
                            if (index % 5 == 0) {
                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = str,
                                    style = TextStyle(
                                        color = textColor,
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
                    // Selection circle using BlendMode.SrcOut to "carve out" the text.
                    drawCircle(
                        color = selectionColor,
                        radius = selectedRadius,
                        center = selected.offset,
                        blendMode = BlendMode.SrcOut
                    )

                    restoreToCount(checkPoint)
                }
            }
        }
    )
}

private fun addHours(
    list: MutableList<TimeOffset>,
    is24h: Boolean,
    canvasWidth: Float,
    canvasHeight: Float,
    selectedRadius: Float,
    padding: Float,
) {
    val clockRadius = canvasHeight / 2
    if (is24h) {
        (0..23).forEach { hour ->
            list.add(
                TimeOffset(
                    time = LocalTime.of(hour, 0),
                    offset = Offset(
                        x = canvasWidth / 2 + cos((hour-6) * 360/24 * PI /180).toFloat() * (clockRadius - selectedRadius - padding),
                        y = canvasHeight / 2 + sin((hour-6) * 360/24 * PI /180).toFloat() * (clockRadius - selectedRadius - padding)
                    )
                )
            )
        }
    }
    else {
        (0..11).forEach { hour ->
            list.add(
                TimeOffset(
                    time = LocalTime.of(hour, 0),
                    offset = Offset(
                        x = canvasWidth / 2 + cos((hour-3) * 360/12 * PI /180).toFloat() * (clockRadius - selectedRadius - padding),
                        y = canvasHeight / 2 + sin((hour-3) * 360/12 * PI /180).toFloat() * (clockRadius - selectedRadius - padding)
                    )
                )
            )
        }
    }
}

private fun addMinutes(
    list: MutableList<TimeOffset>,
    canvasWidth: Float,
    canvasHeight: Float,
    selectedRadius: Float,
    padding: Float,
) {
    val clockRadius = canvasHeight / 2
    (0..59).forEach { minute ->
        list.add(
            TimeOffset(
                time = LocalTime.of(0, minute),
                offset = Offset(
                    x = canvasWidth / 2 + cos((minute-15) * 360/60 * PI /180).toFloat() * (clockRadius - selectedRadius - padding),
                    y = canvasHeight / 2 + sin((minute-15) * 360/60 * PI /180).toFloat() * (clockRadius - selectedRadius - padding)
                )
            )
        )
    }
}