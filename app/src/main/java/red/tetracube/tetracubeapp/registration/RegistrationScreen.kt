package red.tetracube.tetracubeapp.registration

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    val secondScreenResult = navHostController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("scanned_data", "")

    LaunchedEffect(
        key1 = secondScreenResult,
        block = {
            if (secondScreenResult != null && secondScreenResult.value != "") {
                registrationScreenViewModel.updateFormFieldFromQRCode(secondScreenResult.value)
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
    dialogDismissHandler: (ServiceCallStatus) -> Unit,
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
            .fillMaxWidth()
            .fillMaxHeight(),
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

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(10),
                onClick = { onScanQRCodeButtonTap() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_qr_code_scanner_24),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = stringResource(id = R.string.scan_qr_code))
                }
            }
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