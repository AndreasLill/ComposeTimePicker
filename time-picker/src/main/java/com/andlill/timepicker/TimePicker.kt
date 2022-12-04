package com.andlill.timepicker

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TimePickerDialog(
    state: MutableState<Boolean>
) {
    if (state.value) {
        Dialog(
            onDismissRequest = { state.value = false },
            content = {
                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .animateContentSize(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                    }
                }
            }
        )
    }
}