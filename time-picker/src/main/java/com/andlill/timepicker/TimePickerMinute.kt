package com.andlill.timepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerMinute(
    modifier: Modifier,
    colors: TimePickerColors,
    timeSelected: LocalTime,
    readOnly: Boolean,
    selected: Boolean,
    onSelectTime: (LocalTime) -> Unit,
    onSelect: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (!isFocused) {
            text = ""
        }
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (selected) colors.timeDisplayBackgroundSelected else colors.timeDisplayBackground,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isFocused) colors.timeDisplayBorderSelected else colors.timeDisplayBorder
        ),
        onClick = {
            onSelect()
        },
        content = {
            BasicTextField(
                modifier = Modifier
                    .fillMaxSize()
                    .onFocusEvent {
                        isFocused = it.isFocused
                        if (isFocused) {
                            onSelect()
                        }
                    },
                enabled = !readOnly,
                maxLines = 1,
                singleLine = true,
                value = text,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        validateTime(text, timeSelected)?.let(onSelectTime)
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = { value ->
                    if (value.isBlank()) {
                        text = value
                    }
                    else {
                        validateTime(value, timeSelected)?.let { time ->
                            text = value
                            if (value.length >= 2) {
                                onSelectTime(time)
                                focusManager.clearFocus()
                            }
                        }
                    }
                },
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 48.sp,
                    color = colors.timeDisplayText
                ),
                cursorBrush = SolidColor(colors.timeDisplayText),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        innerTextField()
                        if (text.isBlank() && !isFocused) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = timeSelected.toString("mm"),
                                fontSize = 48.sp,
                                color = colors.timeDisplayText
                            )
                        }
                    }
                }
            )
        }
    )
}

private fun validateTime(minute: String, time: LocalTime): LocalTime? {
    return try {
        time.withMinute(minute.toInt())
    }
    catch(ex: Exception) {
        null
    }
}