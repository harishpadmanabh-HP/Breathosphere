package dev.ridill.breathosphere.breathview


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import dev.ridill.breathosphere.BreathConfig
import dev.ridill.breathosphere.MainViewModel

@Composable
fun BreathPulseContainer(
    surfaceColor: Color = Color(0xff1f1f1f),
    pulseColor: Color = Color.Cyan,
    isTimerEnabled: Boolean = true,
    timerTextStyle: TextStyle = MaterialTheme.typography.h4.copy(color = Color.White),
    messageTextStyle: TextStyle = MaterialTheme.typography.h6.copy(color = Color.White),
    breathConfig: BreathConfig = BreathConfig()

) {
    val state: BreathViewState = rememberBreathViewState().also {
        it.setConfig(
            relaxTime = 5,
            cycles = breathConfig.cycle,
            inhale = breathConfig.inhaleTime,
            exhale = breathConfig.exhaleTime,
            inhaleHoldTime = breathConfig.inhaleHoldTime,
            exhaleHoldTime = breathConfig.exhaleHoldTime
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = state.message.collectAsState(initial = "")

        val duration = state.duration.collectAsState(initial = "")

        Text(text = duration.value, style = messageTextStyle)
        Spacer(modifier = Modifier.height(24.dp))
        BreathView(state = state, color = pulseColor, timerTextStyle = timerTextStyle)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = message.value, style = messageTextStyle)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            state.startExercise()
        }) {
            Text(text = "Start Exercise")
        }


    }
}