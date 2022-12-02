package red.tetracube.tetracubeapp.registration.models

data class RegistrationFormData(
    val username: String? = null,
    val password: String? = null,
    val houseName: String? = null,
    val tetracubeHostAddress: String? = null,
    val passwordHidden: Boolean = true,
    val isFormValid: Boolean = false
)