package red.tetracube.tetracubeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import red.tetracube.tetracubeapp.core.extensions.appSettings
import red.tetracube.tetracubeapp.platform.TetraCubeAppPlatform
import red.tetracube.tetracubeapp.platform.TetraCubeAppPlatformViewModel
import red.tetracube.tetracubeapp.platform.TetraCubeAppPlatformViewModelFactory
import red.tetracube.tetracubeapp.ui.theme.TetraCubeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TetraCubeAppTheme {
                val context = LocalContext.current
                val appSettings = context.appSettings
                val tetraCubeAppPlatformViewModel: TetraCubeAppPlatformViewModel = viewModel(
                    factory = TetraCubeAppPlatformViewModelFactory(appSettings)
                )
                TetraCubeAppPlatform(tetraCubeAppPlatformViewModel)
            }
        }
    }
}