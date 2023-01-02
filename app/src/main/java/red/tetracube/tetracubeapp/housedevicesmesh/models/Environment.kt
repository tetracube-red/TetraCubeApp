package red.tetracube.tetracubeapp.housedevicesmesh.models

import red.tetracube.tetracubeapp.data.repositories.remote.smarthome.payloads.HouseEnvironmentResponse
import java.util.*

data class Environment(
    val id: UUID,
    val name: String
) {
    constructor(environmentAPIResponse: HouseEnvironmentResponse) : this(
        id = environmentAPIResponse.id!!,
        name = environmentAPIResponse.name!!
    )
}