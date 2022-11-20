package red.tetracube.tetracubeapp.registration.models

data class RegistrationFormData(
    val username: String? = null,
    val password: String? = null,
    val passwordConfirmation: String? = null,
    val tetracubeHostAddress: String? = null,
    val invitationCode: String? = null,
    val passwordHidden: Boolean = true,
    val passwordConfirmationHidden: Boolean = true,
    val isFormValid: Boolean = false
)