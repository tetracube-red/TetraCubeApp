package red.tetracube.tetracubeapp.data.repositories.remote.housedevicesmesh.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*


class HouseEnvironmentResponse @JsonCreator constructor(
    @JsonProperty
    val id: UUID?,

    @JsonProperty
    val name: String?
)
