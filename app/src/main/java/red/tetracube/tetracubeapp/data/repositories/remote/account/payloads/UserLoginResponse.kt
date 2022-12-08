package red.tetracube.tetracubeapp.data.repositories.remote.account.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class UserLoginResponse @JsonCreator constructor(
    @JsonProperty("id")
    val userId: UUID,

    @JsonProperty("name")
    val username: String,

    @JsonProperty("token")
    val authenticationToken: String,

    @JsonProperty("houseId")
    val houseId: UUID,

    @JsonProperty("houseName")
    val houseName: String
)
