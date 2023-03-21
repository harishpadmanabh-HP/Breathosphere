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
import dev.ridill.breathosphere.MainViewModel

@Composable
fun BreathPulseContainer(
    surfaceColor: Color = Color(0xff1f1f1f),
    pulseColor: Color = Color.Cyan,
    isTimerEnabled: Boolean = true,
    timerTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    messageTextStyle: TextStyle = MaterialTheme.typography.h6.copy(color = Color.White),
    inhaleTime: Int = 4,
    exhaleTime: Int = 4,
    inhaleHold: Int = 0,
    exhaleHold: Int = 0,
) {
    val state: BreathViewState = rememberBreathViewState().also {
        it.setConfig(
            relaxTime = 4,
            cycles = 2,
            inhale = inhaleTime,
            exhale = exhaleTime
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

        BreathView(state = state, color = pulseColor)

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