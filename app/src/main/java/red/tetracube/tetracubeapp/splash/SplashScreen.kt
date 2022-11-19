package red.tetracube.tetracubeapp.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.splash.models.SplashFluxState

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    applicationSettings: TetraCubeSettings,
    splashScreenViewModel: SplashScreenViewModel = viewModel()
) {
    LaunchedEffect(
        key1 = applicationSettings,
        block = {
            splashScreenViewModel.initializeApplication(applicationSettings)
        }
    )

    val splashFluxState = splashScreenViewModel.splashFluxState
    LaunchedEffect(
        key1 = splashFluxState,
        block = {
            when (splashFluxState) {
                SplashFluxState.START -> {}
                SplashFluxState.CHECKING_CONFIGURATION -> {}
                SplashFluxState.SUCCESS -> TODO()
                SplashFluxState.ERROR -> TODO()
                SplashFluxState.MISSING_CONFIGURATION -> {}
                //  navHostController.navigate(LoginScreenRoute.route) {
                //      popUpTo(0)
                //  }
                SplashFluxState.LOGGING_IN -> {

                }
                SplashFluxState.GETTING_TETRACUBE_METADATA -> {}
                SplashFluxState.MISSING_HOSTS -> {}
                // navHostController.navigate(LoginScreenRoute.route) {
                //     popUpTo(0)
                // }
            }
        }
    )

    SplashView(splashFluxState)
}

@Composable
fun SplashView(
    splashFluxState: SplashFluxState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = "HELLO"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            style = MaterialTheme.typography.titleSmall,
            text = stringResource(id = splashFluxState.resourceId)
        )
    }
}