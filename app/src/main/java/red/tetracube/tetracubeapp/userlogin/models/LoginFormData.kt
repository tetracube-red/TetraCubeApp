package red.tetracube.tetracubeapp.userlogin.models

data class LoginFormData(
    val username: String? = null,
    val authenticationCode: String? = null,
    val tetracubeHostAddress: String? = null,
    val isCodeHidden: Boolean = true,
    val isFormValid: Boolean = false
)