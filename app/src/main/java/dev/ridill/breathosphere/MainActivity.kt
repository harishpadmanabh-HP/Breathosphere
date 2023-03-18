package dev.ridill.breathosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.ridill.breathosphere.breathview.BreathPulseContainer
import dev.ridill.breathosphere.breathview.BreathView
import dev.ridill.breathosphere.breathview.rememberBreathViewState
import dev.ridill.breathosphere.ui.theme.BreathosphereTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreathosphereTheme {

                BreathPulseContainer()

//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
////                        val breathosphereState = rememberBreathosphereState()
////                        Breathosphere(breathosphereState = breathosphereState)
//
//                        val breathViewState = rememberBreathViewState()
//
//                        val message = breathViewState.message.collectAsState(initial = "")
//
//                        BreathView(state = breathViewState)
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        Text(text = message.value)
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        Button(onClick = {
//                            breathViewState.startExercise()
//                        }) {
//                            Text(text = "Start Exercise")
//                        }
//                    }
//                }
            }
        }
    }
}

@Composable
fun Breathosphere(
    breathosphereState: BreathosphereState,
    modifier: Modifier = Modifier,
    circleSize: Dp = 300.dp,
    color: Color = MaterialTheme.colors.onSurface
) {
    val currentMode by breathosphereState.currentMode
    val breathCircleFraction by breathosphereState.breathCircleFraction

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
                    radius = if (currentMode == BreathosphereState.Mode.BREATHING)
                        radius * 0.5f
                    else radius * breathCircleFraction
                )
            }
    )
}

class BreathosphereState(
    private val coroutineScope: CoroutineScope
) {
    var currentMode = mutableStateOf(Mode.IDLE)
        private set

    private val breathAnimatable = Animatable(FRACTION_FULL)
    val breathCircleFraction = breathAnimatable.asState()

    fun startExercise(
        relaxationMillis: Long = 5 * 1000,
        breathCount: Int = 3,
        breathInterval: Int = 5 * 1000
    ) = coroutineScope.launch {
        currentMode.value = Mode.RELAXATION

        // Divide relaxation time into 4 equal parts
        // For the 4 sub portions of the animation
        val relaxationSegment = (relaxationMillis / 4).toInt()
        // Initial Delay
        delay(relaxationSegment.toLong())
        // Animate fraction to half
        breathAnimatable.animateTo(FRACTION_HALF, tween(relaxationSegment))
        // Animate fraction to value between 0.5 and 1
        breathAnimatable.animateTo(FRACTION_ALMOST_FULL, tween(relaxationSegment))
        // Animate back to half
        delay(500L)
        breathAnimatable.animateTo(FRACTION_HALF, tween(relaxationSegment))

        currentMode.value = Mode.BREATHING
        // Repeat for number of breaths needed
        repeat(breathCount) { singleBreathAnimation(breathInterval) }
        currentMode.value = Mode.IDLE
        breathAnimatable.animateTo(FRACTION_FULL, tween(breathInterval))
    }

    // Animate fraction from half to full for inhale
    // then from full back to half for exhale
    // With a small delay before each animation
    private suspend fun singleBreathAnimation(interval: Int) {
        delay(500L)
        breathAnimatable.animateTo(FRACTION_FULL, tween(interval))
        delay(500L)
        breathAnimatable.animateTo(FRACTION_HALF, tween(interval))
    }

    // Modes to denote the phases in entire exercise
    enum class Mode { IDLE, RELAXATION, BREATHING }
}

private const val FRACTION_FULL = 1f
private const val FRACTION_ALMOST_FULL = 0.8f
private const val FRACTION_HALF = 0.5f

@Composable
fun rememberBreathosphereState(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): BreathosphereState = remember(coroutineScope) {
    BreathosphereState(coroutineScope)
}