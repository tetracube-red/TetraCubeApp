package red.tetracube.tetracubeapp.data.repositories.remote.guest.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class GuestLoginRequest @JsonCreator constructor(
    @JsonProperty("nickname")
    val nickname: String? = null,

    @JsonProperty("password")
    val password: String? = null,
)