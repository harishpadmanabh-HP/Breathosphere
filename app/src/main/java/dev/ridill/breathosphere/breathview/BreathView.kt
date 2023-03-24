package dev.ridill.breathosphere.breathview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BreathView(
    state: BreathViewState,
    modifier: Modifier = Modifier,
    circleSize: Dp = 200.dp,
    color: Color = Color.Cyan,
    timerTextStyle: TextStyle = MaterialTheme.typography.h4.copy(color = Color.White)
) {
    val currentMode by state.currentMode
    val breathCircleFraction by state.breathCircleFraction
    val timer = state.timer.collectAsState(initial = "")


    Box(
        modifier = modifier
            .size(circleSize)
            .drawBehind {
                val radius = size.minDimension / 2

                // Outer static circle
                drawCircle(
                    color = color.copy(alpha = 0.5f),
                    radius = radius
                )

                // Breathing circle that animates with each breath
                drawCircle(
                    color = color.copy(alpha = 0.25f),
                    radius = radius * breathCircleFraction
                )

                // Relaxation circle
                drawCircle(
                    color = color.copy(alpha = 0.25f),
                    radius = if (currentMode == BreathViewState.Mode.BREATHING)
                        radius * 0.5f
                    else radius * breathCircleFraction
                )
            },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = state.progress.value,
            modifier = Modifier.size(circleSize),
            color = Color.Yellow,
            strokeWidth = 2.dp
        )

        if (currentMode == BreathViewState.Mode.BREATHING)
            Text(text = timer.value, style = timerTextStyle)
    }

}