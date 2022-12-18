package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import red.tetracube.tetracubeapp.core.definitions.DeviceType
import red.tetracube.tetracubeapp.housedevicesmesh.models.Device

@Composable
fun  DeviceItemTrailingWidget(device: Device) {
    if (device.deviceType == DeviceType.SWITCHER) {
        Switch(
            checked = true,
            onCheckedChange = {}
        )
    }
}