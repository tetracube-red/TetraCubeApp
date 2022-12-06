package red.tetracube.tetracubeapp.data.repositories.remote.account.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class UserLoginResponse @JsonCreator constructor(
    @JsonProperty("userId")
    val userId: UUID,

    @JsonProperty("username")
    val username: String,

    @JsonProperty("authenticationToken")
    val authenticationToken: String
)
