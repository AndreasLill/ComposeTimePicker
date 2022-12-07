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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TimePickerTextField(
    modifier: Modifier,
    value: String,
    readOnly: Boolean,
    selected: Boolean,
    imeAction: ImeAction,
    onValueChange: () -> Unit,
    onSelect: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember(value) { mutableStateOf(value) }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            textFieldValue = ""
        }
        if (!isFocused && textFieldValue.isBlank()) {
            textFieldValue = value
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
                    },
                enabled = !readOnly,
                maxLines = 1,
                singleLine = true,
                value = textFieldValue,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = {
                    textFieldValue = it
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
                    }
                }
            )
        }
    )
}