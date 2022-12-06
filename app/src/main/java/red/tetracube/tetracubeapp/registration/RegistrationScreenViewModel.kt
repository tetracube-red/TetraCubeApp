package red.tetracube.tetracubeapp.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.data.repositories.remote.account.AccountResources
import red.tetracube.tetracubeapp.registration.models.FormDataFieldName
import red.tetracube.tetracubeapp.registration.models.RegistrationFormData

class RegistrationScreenViewModel : ViewModel() {

    var registrationFormData by mutableStateOf(RegistrationFormData())
        private set
    var serviceCallStatus by mutableStateOf(ServiceCallStatus.IDLE)
        private set

    private val accountResources = AccountResources()

    fun updateFormFieldValue(field: FormDataFieldName, fieldValue: String?) {
        val updatedFormValue = when (field) {
            FormDataFieldName.USERNAME -> registrationFormData.copy(username = fieldValue)
            FormDataFieldName.AUTHENTICATION_CODE -> registrationFormData.copy(authenticationCode = fieldValue)
            FormDataFieldName.TETRACUBE_HOST_ADDRESS -> registrationFormData.copy(
                tetracubeHostAddress = fieldValue
            )
        }
        val formIsValid = !updatedFormValue.username.isNullOrEmpty()
                && !updatedFormValue.authenticationCode.isNullOrEmpty()
                && !updatedFormValue.tetracubeHostAddress.isNullOrEmpty()

        registrationFormData = updatedFormValue.copy(isFormValid = formIsValid)
    }

    fun fieldTrailingIconClickHandler(field: FormDataFieldName) {
        val updateFieldStatus = when (field) {
            FormDataFieldName.AUTHENTICATION_CODE -> registrationFormData.copy(passwordHidden = !registrationFormData.passwordHidden)
            else -> {
                registrationFormData
            }
        }
        registrationFormData = updateFieldStatus
    }

    suspend fun onRegistrationButtonClickHandler() {
        viewModelScope.launch {
            accountResources.serviceCallStatus
                .collect { serviceCallStatus = it }
        }
        accountResources.accountRegistration(
            registrationFormData.username!!,
            registrationFormData.authenticationCode!!,
            registrationFormData.tetracubeHostAddress!!
        )
    }

    fun resetConnectionStatus() {
        serviceCallStatus = ServiceCallStatus.IDLE
    }
}