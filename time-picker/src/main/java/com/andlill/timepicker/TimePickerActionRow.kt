package com.andlill.timepicker

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TimePickerActionRow(
    onNegativeClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterStart),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        }
        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            TextButton(
                colors = ButtonDefaults.textButtonColors(),
                onClick = onNegativeClick,
                content = {
                    Text("Cancel")
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                colors = ButtonDefaults.textButtonColors(),
                onClick = onPositiveClick,
                content = {
                    Text("OK")
                }
            )
        }
    }
}