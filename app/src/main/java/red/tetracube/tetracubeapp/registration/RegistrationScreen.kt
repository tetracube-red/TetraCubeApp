package red.tetracube.tetracubeapp.registration

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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


    val secondScreenResult = navHostController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("scanned_data", "")

    LaunchedEffect(
        key1 = secondScreenResult,
        block = {
            if (secondScreenResult != null) {
                registrationScreenViewModel.updateFormFieldValue(
                    FormDataFieldName.TETRACUBE_HOST_ADDRESS,
                    secondScreenResult.value
                )
            }
        }
    )

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
        onScanQRCodeButtonTap = {
            navHostController.navigate("qr-scanner")
        },
        dialogDismissHandler = {
            if (it == ServiceCallStatus.FINISHED_SUCCESS) {
                navHostController.popBackStack()
            } else {
                registrationScreenViewModel.resetConnectionStatus()
            }
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
    onScanQRCodeButtonTap: () -> Unit,
    dialogDismissHandler: (ServiceCallStatus) -> Unit
) {
    val passwordTrailingIcon = if (registrationFormData.passwordHidden) {
        R.drawable.round_visibility_24
    } else {
        R.drawable.round_visibility_off_24
    }
    val passwordConfirmationTrailingIcon = if (registrationFormData.passwordConfirmationHidden) {
        R.drawable.round_visibility_24
    } else {
        R.drawable.round_visibility_off_24
    }

    RegistrationServiceDialogs(registrationServiceStatus, dialogDismissHandler)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
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

        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            modifier = Modifier.fillMaxWidth(),
            value = registrationFormData.password ?: "",
            onValueChange = { onFieldUpdate(FormDataFieldName.PASSWORD, it) },
            label = { Text(stringResource(FormDataFieldName.PASSWORD.labelId)) },
            trailingIcon = {
                IconButton(
                    onClick = { onTrailingIconClicked(FormDataFieldName.PASSWORD) },
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
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            value = registrationFormData.passwordConfirmation ?: "",
            onValueChange = { onFieldUpdate(FormDataFieldName.PASSWORD_CONFIRMATION, it) },
            label = {
                Text(
                    stringResource(FormDataFieldName.PASSWORD_CONFIRMATION.labelId),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            },
            trailingIcon = {
                IconButton(
                    onClick = { onTrailingIconClicked(FormDataFieldName.PASSWORD_CONFIRMATION) },
                ) {
                    Icon(
                        painter = painterResource(id = passwordConfirmationTrailingIcon),
                        contentDescription = ""
                    )
                }
            },
            visualTransformation = if (registrationFormData.passwordConfirmationHidden)
                PasswordVisualTransformation()
            else
                VisualTransformation.None
        )

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
            onValueChange = { onFieldUpdate(FormDataFieldName.TETRACUBE_HOST_ADDRESS, it) },
            label = { Text(stringResource(FormDataFieldName.TETRACUBE_HOST_ADDRESS.labelId)) }
        )
        OutlinedTextField(
            singleLine = true,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                autoCorrect = false,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            value = registrationFormData.invitationCode ?: "",
            onValueChange = { onFieldUpdate(FormDataFieldName.INVITATION_CODE, it) },
            label = { Text(stringResource(FormDataFieldName.INVITATION_CODE.labelId)) }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            onClick = { onScanQRCodeButtonTap() }
        ) {
            Text(stringResource(R.string.scan_qr_code))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            enabled = registrationFormData.isFormValid,
            onClick = { onRegistrationButtonClicked() }
        ) {
            Text(stringResource(R.string.start_registration))
        }
    }
}