package red.tetracube.tetracubeapp.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.splash.models.SplashFluxState

class SplashScreenViewModel : ViewModel() {

    var splashFluxState by mutableStateOf(SplashFluxState.START)
        private set

    suspend fun initializeApplication(applicationSettings: TetraCubeSettings) {
        updateSplashFluxState(SplashFluxState.START)
        delay(200)
        updateSplashFluxState(SplashFluxState.CHECKING_CONFIGURATION)
        delay(200)
        if (!applicationSettings.applicationInitialized) {
            updateSplashFluxState(SplashFluxState.MISSING_CONFIGURATION)
            return
        }
        if (applicationSettings.pairedTetracubesCount == 0) {
            updateSplashFluxState(SplashFluxState.MISSING_CONFIGURATION)
            return
        }
        updateSplashFluxState(SplashFluxState.GETTING_TETRACUBE_METADATA)
        delay(200)

        val tetracube = applicationSettings.pairedTetracubesList.firstOrNull { it.isDefault }
            ?: applicationSettings.pairedTetracubesList.first()
        if (tetracube == null) {
            updateSplashFluxState(SplashFluxState.MISSING_CONFIGURATION)
            return
        }
        updateSplashFluxState(SplashFluxState.SUCCESS)
    }

    private fun updateSplashFluxState(splashFluxState: SplashFluxState) {
        this.splashFluxState = splashFluxState
    }
}