package dev.ridill.breathosphere

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.mhssn.colorpicker.ColorPicker
import io.mhssn.colorpicker.ColorPickerType

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BreathConfigScreen(
    viewModel: MainViewModel,
    onNavigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff1f1f1f))
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var color by remember {
            mutableStateOf(Color.Cyan)
        }

        var inhaleTime by remember {
            mutableStateOf(4)
        }

        var exhaleTime by remember {
            mutableStateOf(4)
        }


        var inhaleHold by remember {
            mutableStateOf(0)
        }

        var exhaleHold by remember {
            mutableStateOf(0)
        }

        var cycles by remember {
            mutableStateOf(2)
        }

        var showColorChooser by remember {
            mutableStateOf(false)
        }

        Text(
            text = "Configure Your Breathing Exercise",
            style = MaterialTheme.typography.h6,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xff2c2c2c),
            contentColor = Color.White
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {

                SliderItem(
                    title = "Inhale Time : ",
                    onValueChange = {
                        inhaleTime = it
                    },
                    defaultValue = inhaleTime.toFloat(),
                )
                SliderItem(
                    title = "Exhale Time : ",
                    onValueChange = {
                        exhaleTime = it
                    },
                    defaultValue = exhaleTime.toFloat(),
                )
                SliderItem(
                    title = "Inhale Hold Time : ",
                    onValueChange = {
                        inhaleHold = it
                    },
                    defaultValue = inhaleHold.toFloat(),
                )
                SliderItem(
                    title = "Exhale Hold Time : ",
                    onValueChange = {
                        exhaleHold = it
                    },
                    defaultValue = exhaleHold.toFloat(),
                )
                SliderItem(
                    title = "No. of Breath Cycles : ",
                    unit = "",
                    onValueChange = {
                        cycles = it
                    },
                    defaultValue = cycles.toFloat(),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showColorChooser = true
                }, shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xff2c2c2c),
            contentColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = "Choose pulse color",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(color, shape = CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
               viewModel.updateConfig(
                   inhaleTime, exhaleTime, inhaleHold, exhaleHold,cycles
               )
                onNavigate()
            },
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(text = "Start Exercise", style = MaterialTheme.typography.button)
        }

        AnimatedVisibility(visible = showColorChooser) {

            ColorChooser(
                onDismissed = { showColorChooser = false },
                onColorChose = {
                    color = it
                })
        }

    }
}

@Composable
fun SliderItem(
    title: String,
    defaultValue: Float,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unit: String = " seconds"

) {

    var value by remember {
        mutableStateOf(defaultValue)
    }

    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            text = title + value.toInt() + unit,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Slider(
            value = value,
            onValueChange = {
                value = it
            },
            steps = 20,
            valueRange = 0f..60f,
            onValueChangeFinished = {
                onValueChange(value.toInt())
            },
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ColorChooser(

    onDismissed: () -> Unit,
    onColorChose: (Color) -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissed() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            backgroundColor = Color(0xff2c2c2c),
            contentColor = Color.White
        ) {
            ColorPicker(
                modifier = Modifier.padding(20.dp),
                type = ColorPickerType.Ring(
                    ringWidth = 10.dp,
                    previewRadius = 80.dp,
                    showColorPreview = true
                )
            ) {
                onColorChose(it)
            }

        }
    }
}



