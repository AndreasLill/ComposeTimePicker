package com.andlill.timepicker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun TimePickerPeriodSelector(
    modifier: Modifier,
    colors: TimePickerColors,
    strings: TimePickerStrings,
    vertical: Boolean,
    timePeriod: TimePeriod,
    onSelect: (TimePeriod) -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colors.periodSelectorBorder
        ),
        content = {
            if (vertical) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(
                                if (timePeriod == TimePeriod.AM)
                                    colors.periodSelectorBackgroundSelected
                                else
                                    colors.periodSelectorBackground
                            )
                            .clickable {
                                onSelect(TimePeriod.AM)
                            },
                        contentAlignment = Alignment.Center) {
                        TimePickerPeriodSelectorText(
                            text = strings.periodSelectorAM,
                            colors = colors,
                        )
                    }
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.periodSelectorBorder
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(
                                if (timePeriod == TimePeriod.PM)
                                    colors.periodSelectorBackgroundSelected
                                else
                                    colors.periodSelectorBackground
                            )
                            .clickable {
                                onSelect(TimePeriod.PM)
                            },
                        contentAlignment = Alignment.Center) {
                        TimePickerPeriodSelectorText(
                            text = strings.periodSelectorPM,
                            colors = colors,
                        )
                    }
                }
            }
            else {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (timePeriod == TimePeriod.AM)
                                    colors.periodSelectorBackgroundSelected
                                else
                                    colors.periodSelectorBackground
                            )
                            .clickable {
                                onSelect(TimePeriod.AM)
                            },
                        contentAlignment = Alignment.Center) {
                        TimePickerPeriodSelectorText(
                            text = strings.periodSelectorAM,
                            colors = colors,
                        )
                    }
                    Divider(
                        modifier = Modifier.fillMaxHeight().width(1.dp),
                        color = colors.periodSelectorBorder
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (timePeriod == TimePeriod.PM)
                                    colors.periodSelectorBackgroundSelected
                                else
                                    colors.periodSelectorBackground
                            )
                            .clickable {
                                onSelect(TimePeriod.PM)
                            },
                        contentAlignment = Alignment.Center) {
                        TimePickerPeriodSelectorText(
                            text = strings.periodSelectorPM,
                            colors = colors,
                        )
                    }
                }
            }
        }
    )
}

@Composable
internal fun TimePickerPeriodSelectorText(
    text: String,
    colors: TimePickerColors
) {
    Text(
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = colors.periodSelectorText
    )
}