package red.tetracube.tetracubeapp.core.extensions

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettingsSerializer

val Context.appSettings: DataStore<TetraCubeSettings> by dataStore(
    fileName = "tetracube_settings.proto",
    serializer = TetraCubeSettingsSerializer
)