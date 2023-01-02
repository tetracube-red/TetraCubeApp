package red.tetracube.tetracubeapp.guest.login.models

data class LoginFormData(
    val nickname: String? = null,
    val password: String? = null,
    val tetraCubeHostAddress: String? = null,
    val isCodeHidden: Boolean = true,
    val isFormValid: Boolean = false
)