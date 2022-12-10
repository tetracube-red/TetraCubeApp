package red.tetracube.tetracubeapp.housedevicesmesh

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.data.repositories.remote.housedevicesmesh.HouseDevicesMeshAPIClient
import red.tetracube.tetracubeapp.housedevicesmesh.models.Device

class HouseDevicesMeshViewModel : ViewModel() {

    var serviceCallStatus by mutableStateOf(ServiceCallStatus.IDLE)
        private set
    var houseName by mutableStateOf("")
        private set
    val devices = mutableStateListOf<Device>()
    var refreshing by mutableStateOf(false)
        private set

    suspend fun getHouseMeshDescription(tetraCube: PairedTetraCube) {
        houseName = tetraCube.houseName
        val houseDevicesMeshAPIClient = HouseDevicesMeshAPIClient(
            tetraCube.host,
            tetraCube.authenticationToken
        )
        viewModelScope.launch {
            houseDevicesMeshAPIClient.serviceCallStatus
                .collect {
                    when (it) {
                        ServiceCallStatus.CONNECTING -> {
                            refreshing = true
                        }
                       /* ServiceCallStatus.FINISHED_SUCCESS, ServiceCallStatus.IDLE -> {
                            refreshing = false
                        }*/
                        else -> {
                            serviceCallStatus = it
                            refreshing = false
                        }
                    }
                }
        }
        val getHouseMeshDescriptionResponse = houseDevicesMeshAPIClient.getHouseMeshDescription()
            ?: return
        val mappedDevices = getHouseMeshDescriptionResponse.houseMeshDeviceResponseList
            ?.map { d -> Device(d) }
            ?.toMutableList()
        if (mappedDevices != null) {
            devices.clear()
            devices.addAll(mappedDevices)
        }
    }
}