package dev.ridill.breathosphere

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var breathConfig by mutableStateOf(BreathConfig())

    fun updateConfig(
        inhaleTime: Int,
        exhaleTime: Int,
        inhaleHold: Int,
        exhaleHold: Int,
        cycles: Int,
        pulseColor: Color
    ) {
        breathConfig =
            BreathConfig(inhaleTime, exhaleTime, inhaleHold, exhaleHold, cycles, pulseColor)

    }

}