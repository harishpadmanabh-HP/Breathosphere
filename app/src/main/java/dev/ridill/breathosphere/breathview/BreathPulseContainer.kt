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

@Composable
fun BreathPulseContainer(
    surfaceColor: Color = MaterialTheme.colors.surface,
    pulseColor: Color = MaterialTheme.colors.primary,
    isTimerEnabled: Boolean = true,
    timerTextStyle: TextStyle = MaterialTheme.typography.subtitle1,
    messageTextStyle: TextStyle = MaterialTheme.typography.h6,
    state: BreathViewState = rememberBreathViewState()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message = state.message.collectAsState(initial = "")

        BreathView(state = state)

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = message.value)

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            state.startExercise()
        }) {
            Text(text = "Start Exercise")
        }


    }
}