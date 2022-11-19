package red.tetracube.tetracubeapp.platform

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings


class TetraCubeAppPlatformViewModel(private val appSettings: DataStore<TetraCubeSettings>) :
    ViewModel() {

    var applicationSettings by mutableStateOf(TetraCubeSettings.getDefaultInstance())
        private set

    suspend fun loadApplicationData() {
        appSettings.data.collect {
            applicationSettings = it
        }
    }
}

class TetraCubeAppPlatformViewModelFactory(private val appSettings: DataStore<TetraCubeSettings>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TetraCubeAppPlatformViewModel(appSettings) as T
    }
}
