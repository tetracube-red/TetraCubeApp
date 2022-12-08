package red.tetracube.tetracubeapp.userlogin

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.appSettings
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.data.repositories.remote.account.UserAPIClient
import red.tetracube.tetracubeapp.data.repositories.remote.account.payloads.UserLoginResponse
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

    suspend fun onLoginButtonTapHandler(context: Context) {
        viewModelScope.launch {
            userAPIClient.serviceCallStatus
                .collect { serviceCallStatus = it }
        }
        val userLoginResponse: UserLoginResponse = userAPIClient.userLogin(
            userLoginFormData.username!!,
            userLoginFormData.authenticationCode!!,
            userLoginFormData.tetracubeHostAddress!!
        ) ?: return

        context.appSettings.updateData { appSettings ->
            val tetracubeExists =
                appSettings.pairedTetracubesList.any { t -> t.host == userLoginFormData.tetracubeHostAddress }
            val tetracube = if (tetracubeExists)
                appSettings.pairedTetracubesList.first { t -> t.host == userLoginFormData.tetracubeHostAddress }
            else
                PairedTetraCube.getDefaultInstance()
                    .toBuilder()
                    .setAccountId(userLoginResponse.userId.toString())
                    .setAccountName(userLoginResponse.username)
                    .setAuthenticationToken(userLoginResponse.authenticationToken)
                    .setHost(userLoginFormData.tetracubeHostAddress!!)
                    .setHouseId(userLoginResponse.houseId.toString())
                    .setHouseName(userLoginResponse.houseName)
                    .build()
            val tetracubeIdx =
                appSettings.pairedTetracubesList.indexOfFirst { t -> t.host == userLoginFormData.tetracubeHostAddress }
            if (!tetracubeExists) {
                appSettings.toBuilder()
                    .setApplicationInitialized(true)
                    .addPairedTetracubes(tetracube)
                    .build()
            } else {
                appSettings.toBuilder()
                    .setApplicationInitialized(true)
                    .setPairedTetracubes(tetracubeIdx, tetracube)
                    .build()
            }
        }
    }

    fun resetConnectionStatus() {
        serviceCallStatus = ServiceCallStatus.IDLE
    }
}