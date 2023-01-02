package red.tetracube.tetracubeapp.data.repositories.remote.guest.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class GuestLoginResponse @JsonCreator constructor(
    @JsonProperty("token")
    val authenticationToken: String
)
