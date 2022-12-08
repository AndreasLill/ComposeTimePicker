package com.andlill.timepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
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
internal fun TimePickerHour(
    modifier: Modifier,
    focusRequester: FocusRequester,
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
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Transparent
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
                    }
                    .focusRequester(focusRequester),
                enabled = !readOnly,
                maxLines = 1,
                singleLine = true,
                value = text,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        validateTime(text, timeSelected)?.let(onSelectTime)
                        focusManager.moveFocus(FocusDirection.Next)
                    },
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
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center) {
                        innerTextField()
                        if (text.isBlank() && !isFocused) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = timeSelected.toString("HH", Locale.getDefault()),
                                fontSize = 48.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    )
}

private fun validateTime(hour: String, time: LocalTime): LocalTime? {
    return try {
        time.withHour(hour.toInt())
    }
    catch(ex: Exception) {
        null
    }
}