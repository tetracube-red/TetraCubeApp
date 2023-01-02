package red.tetracube.tetracubeapp.data.repositories.remote.smarthome.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


data class GetHouseMeshDescriptionResponse @JsonCreator constructor(
    @JsonProperty
    val houseId: UUID,

    @JsonProperty
    val name: String,

    @JsonProperty
    val houseMeshDeviceResponseList: List<HouseMeshDeviceResponse>?
)
