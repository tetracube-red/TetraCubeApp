package red.tetracube.tetracubeapp.data.repositories.remote.account.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RegistrationRequest @JsonCreator constructor(
    @JsonProperty("username")
    val username: String? = null,

    @JsonProperty("authenticationToken")
    val authenticationToken: String? = null,

    @JsonProperty("houseName")
    val houseName: String? = null,
)