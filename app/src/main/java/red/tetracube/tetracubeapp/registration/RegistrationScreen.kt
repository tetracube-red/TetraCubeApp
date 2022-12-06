package red.tetracube.tetracubeapp.registration

import androidx.compose.animation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.ktor.client.request.forms.*
import kotlinx.coroutines.launch
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.registration.models.FormDataFieldName
import red.tetracube.tetracubeapp.registration.models.RegistrationFormData

@Composable
fun RegistrationScreen(
    navHostController: NavHostController,
    registrationScreenViewModel: RegistrationScreenViewModel = viewModel()
) {
    val registrationFormData = registrationScreenViewModel.registrationFormData
    val registrationServiceStatus = registrationScreenViewModel.serviceCallStatus
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    RegistrationScreenView(
        registrationFormData = registrationFormData,
        registrationServiceStatus = registrationServiceStatus,
        onFieldUpdate = registrationScreenViewModel::updateFormFieldValue,
        onTrailingIconClicked = registrationScreenViewModel::fieldTrailingIconClickHandler,
        onRegistrationButtonClicked = {
            coroutineScope.launch {
                registrationScreenViewModel.onRegistrationButtonClickHandler()
            }
        },
        dialogDismissHandler = {
            if (it == ServiceCallStatus.FINISHED_SUCCESS) {
                navHostController.popBackStack()
            } else {
                registrationScreenViewModel.resetConnectionStatus()
            }
        },
        onOutsideTap = {
            focusManager.clearFocus()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreenView(
    registrationFormData: RegistrationFormData,
    registrationServiceStatus: ServiceCallStatus,
    onFieldUpdate: (FormDataFieldName, String) -> Unit,
    onTrailingIconClicked: (FormDataFieldName) -> Unit,
    onRegistrationButtonClicked: () -> Unit,
    dialogDismissHandler: (ServiceCallStatus) -> Unit,
    onOutsideTap: () -> Unit
) {
    val passwordTrailingIcon = if (registrationFormData.passwordHidden) {
        R.drawable.round_visibility_24
    } else {
        R.drawable.round_visibility_off_24
    }

    RegistrationServiceDialogs(registrationServiceStatus, dialogDismissHandler)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    onOutsideTap()
                })
            },
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                value = registrationFormData.username ?: "",
                onValueChange = { onFieldUpdate(FormDataFieldName.USERNAME, it) },
                label = { Text(stringResource(id = FormDataFieldName.USERNAME.labelId)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrect = false
                ),
                modifier = Modifier.fillMaxWidth(),
                value = registrationFormData.authenticationCode ?: "",
                onValueChange = { onFieldUpdate(FormDataFieldName.AUTHENTICATION_CODE, it) },
                label = { Text(stringResource(FormDataFieldName.AUTHENTICATION_CODE.labelId)) },
                trailingIcon = {
                    IconButton(
                        onClick = { onTrailingIconClicked(FormDataFieldName.AUTHENTICATION_CODE) },
                    ) {
                        Icon(
                            painter = painterResource(id = passwordTrailingIcon),
                            contentDescription = ""
                        )
                    }
                },
                visualTransformation = if (registrationFormData.passwordHidden)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Uri
                ),
                value = registrationFormData.tetracubeHostAddress ?: "",
                onValueChange = {
                    onFieldUpdate(
                        FormDataFieldName.TETRACUBE_HOST_ADDRESS,
                        it
                    )
                },
                label = { Text(stringResource(FormDataFieldName.TETRACUBE_HOST_ADDRESS.labelId)) }
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = registrationFormData.isFormValid,
            onClick = { onRegistrationButtonClicked() }
        ) {
            Text(stringResource(R.string.start_registration))
        }
    }
}