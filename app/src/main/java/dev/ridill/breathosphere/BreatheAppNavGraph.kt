package dev.ridill.breathosphere

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.ridill.breathosphere.breathview.BreathPulseContainer

const val ConfigScreen = "/config"
const val PulseScreen = "/pulse"

@Composable
fun BreatheAppNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel,
) {
    NavHost(navController = navController, startDestination = ConfigScreen) {
        composable(ConfigScreen) {
            BreathConfigScreen(onNavigate = {
                navController.navigate(PulseScreen)
            }, viewModel = viewModel)
        }
        composable(PulseScreen) {
            BreathPulseContainer(
                surfaceColor = viewModel.breathConfig.surfaceColor,
                pulseColor = viewModel.breathConfig.pulseColor,
                breathConfig = viewModel.breathConfig
            )
        }
    }
}