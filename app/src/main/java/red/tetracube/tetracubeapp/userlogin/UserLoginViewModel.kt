package red.tetracube.tetracubeapp.userlogin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.data.repositories.remote.account.UserAPIClient
import red.tetracube.tetracubeapp.userlogin.models.FormFieldName
import red.tetracube.tetracubeapp.userlogin.models.LoginFormData

class UserLoginViewModel : ViewModel() {

    var userLoginFormData by mutableStateOf(LoginFormData())
        private set
    var serviceCallStatus by mutableStateOf(ServiceCallStatus.IDLE)
        private set

    private val userAPIClient = UserAPIClient()

    fun updateFormFieldValue(field: FormFieldName, fieldValue: String?) {
        val updatedFormValue = when (field) {
            FormFieldName.USERNAME -> userLoginFormData.copy(username = fieldValue)
            FormFieldName.AUTHENTICATION_CODE -> userLoginFormData.copy(authenticationCode = fieldValue)
            FormFieldName.TETRACUBE_HOST_ADDRESS -> userLoginFormData.copy(
                tetracubeHostAddress = fieldValue
            )
        }
        val formIsValid = !updatedFormValue.username.isNullOrEmpty()
                && !updatedFormValue.authenticationCode.isNullOrEmpty()
                && !updatedFormValue.tetracubeHostAddress.isNullOrEmpty()

        userLoginFormData = updatedFormValue.copy(isFormValid = formIsValid)
    }

    fun fieldTrailingIconClickHandler(field: FormFieldName) {
        val updateFieldStatus = when (field) {
            FormFieldName.AUTHENTICATION_CODE -> userLoginFormData.copy(isCodeHidden = !userLoginFormData.isCodeHidden)
            else -> {
                userLoginFormData
            }
        }
        userLoginFormData = updateFieldStatus
    }

    suspend fun onLoginButtonTapHandler() {
        viewModelScope.launch {
            userAPIClient.serviceCallStatus
                .collect { serviceCallStatus = it }
        }
        userAPIClient.userLogin(
            userLoginFormData.username!!,
            userLoginFormData.authenticationCode!!,
            userLoginFormData.tetracubeHostAddress!!
        )
    }

    fun resetConnectionStatus() {
        serviceCallStatus = ServiceCallStatus.IDLE
    }
}