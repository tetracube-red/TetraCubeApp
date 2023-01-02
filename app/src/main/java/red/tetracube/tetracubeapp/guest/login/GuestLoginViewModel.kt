package red.tetracube.tetracubeapp.guest.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.jwt.JWT
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.core.extensions.appSettings
import red.tetracube.tetracubeapp.core.settings.PairedTetraCube
import red.tetracube.tetracubeapp.data.repositories.remote.guest.GuestAPIClient
import red.tetracube.tetracubeapp.data.repositories.remote.guest.payloads.GuestLoginResponse
import red.tetracube.tetracubeapp.guest.login.models.FormFieldName
import red.tetracube.tetracubeapp.guest.login.models.LoginFormData

class GuestLoginViewModel : ViewModel() {

    var userLoginFormData by mutableStateOf(LoginFormData())
        private set
    var serviceCallStatus by mutableStateOf(ServiceCallStatus.IDLE)
        private set

    private val userAPIClient = GuestAPIClient()

    fun updateFormFieldValue(field: FormFieldName, fieldValue: String?) {
        val updatedFormValue = when (field) {
            FormFieldName.NICKNAME -> userLoginFormData.copy(nickname = fieldValue)
            FormFieldName.PASSWORD -> userLoginFormData.copy(password = fieldValue)
            FormFieldName.TETRACUBE_HOST_ADDRESS -> userLoginFormData.copy(
                tetraCubeHostAddress = fieldValue
            )
        }
        val formIsValid = !updatedFormValue.nickname.isNullOrEmpty()
                && !updatedFormValue.password.isNullOrEmpty()
                && !updatedFormValue.tetraCubeHostAddress.isNullOrEmpty()

        userLoginFormData = updatedFormValue.copy(isFormValid = formIsValid)
    }

    fun fieldTrailingIconClickHandler(field: FormFieldName) {
        val updateFieldStatus = when (field) {
            FormFieldName.PASSWORD -> userLoginFormData.copy(isCodeHidden = !userLoginFormData.isCodeHidden)
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
        val userLoginResponse: GuestLoginResponse = userAPIClient.userLogin(
            userLoginFormData.nickname!!,
            userLoginFormData.password!!,
            userLoginFormData.tetraCubeHostAddress!!
        ) ?: return
        val decodedToken = JWT.decode(userLoginResponse.authenticationToken)
        val guestId =
            if (decodedToken.claims["userId"]?.isMissing != false) "" else decodedToken.claims["userId"]!!.asString()
        val guestNickname =
            if (decodedToken.claims["upn"]?.isMissing != false) "" else decodedToken.claims["upn"]!!.asString()
        val houseId =
            if (decodedToken.claims["houseId"]?.isMissing != false) "" else decodedToken.claims["houseId"]!!.asString()
        val houseName =
            if (decodedToken.claims["houseName"]?.isMissing != false) "" else decodedToken.claims["houseName"]!!.asString()
        context.appSettings.updateData { appSettings ->
            val tetraCubeExists =
                appSettings.pairedTetracubesList.any { t -> t.host == userLoginFormData.tetraCubeHostAddress }
            val tetraCube = if (tetraCubeExists)
                appSettings.pairedTetracubesList.first { t -> t.host == userLoginFormData.tetraCubeHostAddress }
            else
                PairedTetraCube.getDefaultInstance()
                    .toBuilder()
                    .setGuestId(guestId)
                    .setGuestNickname(guestNickname)
                    .setAuthenticationToken(userLoginResponse.authenticationToken)
                    .setHost(userLoginFormData.tetraCubeHostAddress!!)
                    .setHouseId(houseId)
                    .setHouseName(houseName)
                    .build()
            val tetraCubeIdx =
                appSettings.pairedTetracubesList.indexOfFirst { t -> t.host == userLoginFormData.tetraCubeHostAddress }
            if (!tetraCubeExists) {
                appSettings.toBuilder()
                    .setApplicationInitialized(true)
                    .addPairedTetracubes(tetraCube)
                    .build()
            } else {
                appSettings.toBuilder()
                    .setApplicationInitialized(true)
                    .setPairedTetracubes(tetraCubeIdx, tetraCube)
                    .build()
            }
        }
    }

    fun resetConnectionStatus() {
        serviceCallStatus = ServiceCallStatus.IDLE
    }
}