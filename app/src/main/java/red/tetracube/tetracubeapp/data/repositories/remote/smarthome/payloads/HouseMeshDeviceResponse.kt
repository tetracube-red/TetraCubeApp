package red.tetracube.tetracubeapp.data.repositories.remote.smarthome.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import red.tetracube.tetracubeapp.core.definitions.DeviceType
import java.util.*

data class HouseMeshDeviceResponse @JsonCreator constructor(
    @JsonProperty
    val id: UUID?,

    @JsonProperty
    val name: String?,

    @JsonProperty
    val colorCode: String?,

    @JsonProperty
    val isOnline: Boolean?,

    @JsonProperty
    val deviceType: DeviceType?,

    @JsonProperty
    val environment: HouseEnvironmentResponse?
)
