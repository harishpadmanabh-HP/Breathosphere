package dev.ridill.breathosphere

import androidx.compose.ui.graphics.Color

data class BreathConfig(
    val inhaleTime: Int = 4,
    val exhaleTime: Int = 4,
    val inhaleHoldTime: Int = 0,
    val exhaleHoldTime: Int = 0,
    val cycle :Int = 2,
    val pulseColor: Color = Color.Cyan,
    val surfaceColor:Color = Color(0xff1f1f1f)
)