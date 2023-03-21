package dev.ridill.breathosphere.breathview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import dev.ridill.breathosphere.BreathosphereState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val FRACTION_FULL = 1f
private const val FRACTION_ALMOST_FULL = 0.8f
private const val FRACTION_HALF = 0.5f

class BreathViewState(
    private val coroutineScope: CoroutineScope
) {

    // Modes to denote the phases in entire exercise
    enum class Mode { IDLE, RELAXATION, BREATHING }

    private var _message = Channel<String>()
    val message = _message.receiveAsFlow()

    private var _timer = Channel<String>()
    val timer = _timer.receiveAsFlow()

    private var _duaration = Channel<String>()
    val duration = _duaration.receiveAsFlow()

    var currentMode = mutableStateOf(Mode.IDLE)
        private set

    var relaxationTime = mutableStateOf(5) //seconds
        private set

    var breathCount = mutableStateOf(3) // breath cycles
        private set

    var inhaleTime = mutableStateOf(4) // seconds
        private set

    var exhaleTime = mutableStateOf(4) // seconds
        private set

    var totalDuration = mutableStateOf(
        (inhaleTime.value + exhaleTime.value) * breathCount.value
    )


    private val breathAnimatable = Animatable(FRACTION_FULL)
    val breathCircleFraction = breathAnimatable.asState()

    // Animate fraction from half to full for inhale
    // then from full back to half for exhale
    // With a small delay before each animation
    private suspend fun singleBreathAnimation(inhaleTime: Int, exhaleTime: Int) {
        var inhaleTimer = inhaleTime
        var exhaleTimer = exhaleTime


        //  delay(500L)
        sendMessage("Breathe In")
        coroutineScope.launch {
            repeat(inhaleTime) {
                updateTimer(inhaleTimer--.toString())
                delay(1000)
            }
        }
        breathAnimatable.animateTo(FRACTION_FULL, tween(inhaleTime * 1000, easing = LinearEasing))
        //   delay(500L)
        sendMessage("Breathe Out")
        coroutineScope.launch {
            repeat(exhaleTime) {
                updateTimer(exhaleTimer--.toString())
                delay(1000L)
            }
        }
        breathAnimatable.animateTo(FRACTION_HALF, tween(exhaleTime * 1000, easing = LinearEasing))

    }

    private fun sendMessage(message: String) = coroutineScope.launch {
        _message.send(message)
    }

    private fun updateTimer(time: String) = coroutineScope.launch {
        _timer.send(time)
    }

    private fun updateDuration(time: String) = coroutineScope.launch {
        _duaration.send(time)
    }

    fun setConfig(
        relaxTime: Int,
        cycles: Int,
        inhale: Int,
        exhale: Int
    ) {
        this.apply {
            relaxationTime.value = relaxTime
            breathCount.value = cycles
            inhaleTime.value = inhale
            exhaleTime.value = exhale
        }

    }

    fun startExercise(
        totalTime: Int = (inhaleTime.value + exhaleTime.value) * breathCount.value
    ) = coroutineScope.launch {

        var duration = totalTime

        currentMode.value = Mode.RELAXATION

        sendMessage("Focus")

        // Divide relaxation time into 4 equal parts
        // For the 4 sub portions of the animation
        val relaxationSegment = ((relaxationTime.value * 1000) / 4).toInt()
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

        launch {
            repeat(totalTime){
                updateDuration(duration--.asHMS())
                delay(1000L)
            }
        }

        // Repeat for number of breaths needed
        repeat(breathCount.value) {
            singleBreathAnimation(
                inhaleTime.value, exhaleTime.value
            )
        }

        currentMode.value = Mode.IDLE
        sendMessage("You have finished your Exercise")
        breathAnimatable.animateTo(FRACTION_FULL, tween(exhaleTime.value * 1000))


    }

    private fun Int.asHMS(): String {
        val hours = this / 3600;
       val  minutes = (this % 3600) / 60;
       val  seconds = this % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}

@Composable
fun rememberBreathViewState(
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): BreathViewState = remember(coroutineScope) {
    BreathViewState(coroutineScope)
}

