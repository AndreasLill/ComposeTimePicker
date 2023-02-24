package com.andlill.timepicker

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

object TimePickerDefaults {

    @Composable
    fun colors(
        background: Color = MaterialTheme.colorScheme.surface,
        timeDisplayBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
        timeDisplayBackgroundSelected: Color = MaterialTheme.colorScheme.primaryContainer,
        timeDisplayBorder: Color = Color.Transparent,
        timeDisplayBorderSelected: Color = MaterialTheme.colorScheme.primary,
        timeDisplayText: Color = MaterialTheme.colorScheme.onSurface,
        periodSelectorBackground: Color = Color.Transparent,
        periodSelectorBackgroundSelected: Color = MaterialTheme.colorScheme.primaryContainer,
        periodSelectorBorder: Color = MaterialTheme.colorScheme.outlineVariant,
        periodSelectorText: Color = MaterialTheme.colorScheme.onSurface,
        clockDialBackground: Color = MaterialTheme.colorScheme.surfaceVariant,
        clockDialSelection: Color = MaterialTheme.colorScheme.primary,
        clockDialSelectionText: Color = MaterialTheme.colorScheme.onPrimary,
        clockDialText: Color = MaterialTheme.colorScheme.onSurface,
        negativeButton: ButtonColors = ButtonDefaults.textButtonColors(),
        positiveButton: ButtonColors = ButtonDefaults.textButtonColors(),
    ): TimePickerColors = TimePickerColors(
        background = background,
        timeDisplayBackground = timeDisplayBackground,
        timeDisplayBackgroundSelected = timeDisplayBackgroundSelected,
        timeDisplayBorder = timeDisplayBorder,
        timeDisplayBorderSelected = timeDisplayBorderSelected,
        timeDisplayText = timeDisplayText,
        periodSelectorBackground = periodSelectorBackground,
        periodSelectorBackgroundSelected = periodSelectorBackgroundSelected,
        periodSelectorBorder = periodSelectorBorder,
        periodSelectorText = periodSelectorText,
        clockDialBackground = clockDialBackground,
        clockDialSelection = clockDialSelection,
        clockDialSelectionText = clockDialSelectionText,
        clockDialText = clockDialText,
        negativeButton = negativeButton,
        positiveButton = positiveButton,
    )

    @Composable
    fun strings(
        periodSelectorAM: String = "AM",
        periodSelectorPM: String = "PM",
        negativeButtonText: String = "Cancel",
        positiveButtonText: String = "OK",
    ): TimePickerStrings = TimePickerStrings(
        periodSelectorAM = periodSelectorAM,
        periodSelectorPM = periodSelectorPM,
        negativeButtonText = negativeButtonText,
        positiveButtonText = positiveButtonText,
    )
}

@Immutable
class TimePickerColors internal constructor(
    val background: Color,
    val timeDisplayBackground: Color,
    val timeDisplayBackgroundSelected: Color,
    val timeDisplayBorder: Color,
    val timeDisplayBorderSelected: Color,
    val timeDisplayText: Color,
    val periodSelectorBackground: Color,
    val periodSelectorBackgroundSelected: Color,
    val periodSelectorBorder: Color,
    val periodSelectorText: Color,
    val clockDialBackground: Color,
    val clockDialSelection: Color,
    val clockDialSelectionText: Color,
    val clockDialText: Color,
    val negativeButton: ButtonColors,
    val positiveButton: ButtonColors,
)

@Immutable
class TimePickerStrings internal constructor(
    val periodSelectorAM: String,
    val periodSelectorPM: String,
    val negativeButtonText: String,
    val positiveButtonText: String,
)