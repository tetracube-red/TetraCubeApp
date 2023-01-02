package red.tetracube.tetracubeapp.housedevicesmesh.models

import androidx.compose.ui.graphics.Color
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.definitions.DeviceType
import red.tetracube.tetracubeapp.data.repositories.remote.smarthome.payloads.HouseMeshDeviceResponse
import java.util.*

data class Device(
    val id: UUID,
    val name: String,
    val colorCode: String,
    val isOnline: Boolean,
    val deviceType: DeviceType,
    val deviceTypeIcon: Int,
    val connectionStatusColor: Color,
    val environment: Environment
) {
    constructor(houseMeshDeviceResponse: HouseMeshDeviceResponse) : this(
        id = houseMeshDeviceResponse.id!!,
        name = houseMeshDeviceResponse.name!!,
        colorCode = houseMeshDeviceResponse.colorCode!!,
        isOnline = houseMeshDeviceResponse.isOnline!!,
        deviceType = houseMeshDeviceResponse.deviceType!!,
        environment = Environment(houseMeshDeviceResponse.environment!!),
        connectionStatusColor = if (houseMeshDeviceResponse.isOnline)
            Color.Green
        else
            Color.Red,
        deviceTypeIcon = when (houseMeshDeviceResponse.deviceType) {
            DeviceType.SWITCHER -> R.drawable.round_power_settings_new_24
            DeviceType.RGB_LED -> R.drawable.round_tips_and_updates_24
        }
    )
}