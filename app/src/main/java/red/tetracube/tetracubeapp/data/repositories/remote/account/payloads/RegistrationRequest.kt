package red.tetracube.tetracubeapp.data.repositories.remote.account.payloads

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RegistrationRequest @JsonCreator constructor(
    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("password")
    val password: String? = null,

    @JsonProperty("invitation_code")
    val invitationCode: String? = null,
)