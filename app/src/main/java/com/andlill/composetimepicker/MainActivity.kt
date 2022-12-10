package com.andlill.composetimepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andlill.composetimepicker.ui.theme.ComposeTimePickerTheme
import com.andlill.timepicker.TimePickerDialog
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTimePickerTheme {
                val dialogState = rememberSaveable { mutableStateOf(false) }
                val time = rememberSaveable { mutableStateOf(LocalTime.now().withSecond(0).withNano(0)) }
                val is24h = rememberSaveable { mutableStateOf(true) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    text = time.value.toString(),
                                    fontSize = 32.sp
                                )
                                Button(onClick = {
                                    is24h.value = false
                                    dialogState.value = true
                                }) {
                                    Text("Pick a time (12h)")
                                }
                                Button(onClick = {
                                    is24h.value = true
                                    dialogState.value = true
                                }) {
                                    Text("Pick a time (24h)")
                                }
                                TimePickerDialog(
                                    state = dialogState,
                                    initialTime = time.value,
                                    is24h = is24h.value,
                                    onSelectTime = {
                                        time.value = it
                                    }
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}