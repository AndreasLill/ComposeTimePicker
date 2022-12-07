package com.andlill.composetimepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.andlill.composetimepicker.ui.theme.ComposeTimePickerTheme
import com.andlill.timepicker.TimePickerDialog
import java.time.LocalTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTimePickerTheme {
                val dialogState = rememberSaveable { mutableStateOf(false) }
                val time = rememberSaveable { mutableStateOf(LocalTime.now()) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    content = {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = time.value.toString())
                                Button(onClick = { dialogState.value = true }) {
                                    Text("Pick a time")
                                }
                                TimePickerDialog(
                                    state = dialogState,
                                    initialTime = time.value,
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