package red.tetracube.tetracubeapp.guest.login

import androidx.compose.animation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
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
import red.tetracube.tetracubeapp.guest.login.models.FormFieldName
import red.tetracube.tetracubeapp.guest.login.models.LoginFormData
import red.tetracube.tetracubeapp.splash.SplashScreenRoute

@Composable
fun GuestLoginScreen(
    navHostController: NavHostController,
    guestLoginViewModel: GuestLoginViewModel = viewModel()
) {
    val loginFormData = guestLoginViewModel.userLoginFormData
    val loginServiceStatus = guestLoginViewModel.serviceCallStatus
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    GuestLoginScreenView(
        loginFormData = loginFormData,
        loginServiceStatus = loginServiceStatus,
        onFieldUpdate = guestLoginViewModel::updateFormFieldValue,
        onTrailingIconClicked = guestLoginViewModel::fieldTrailingIconClickHandler,
        onLoginButtonClicked = {
            coroutineScope.launch {
                guestLoginViewModel.onLoginButtonTapHandler(context)
            }
        },
        dialogDismissHandler = {
            if (it == ServiceCallStatus.FINISHED_SUCCESS) {
                navHostController.navigate(SplashScreenRoute.route) {
                    popUpTo(0)
                }
            } else {
                guestLoginViewModel.resetConnectionStatus()
            }
        },
        onOutsideTap = {
            focusManager.clearFocus()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestLoginScreenView(
    loginFormData: LoginFormData,
    loginServiceStatus: ServiceCallStatus,
    onFieldUpdate: (FormFieldName, String) -> Unit,
    onTrailingIconClicked: (FormFieldName) -> Unit,
    onLoginButtonClicked: () -> Unit,
    dialogDismissHandler: (ServiceCallStatus) -> Unit,
    onOutsideTap: () -> Unit
) {
    val authenticationCodeTrailingIcon = if (loginFormData.isCodeHidden) {
        R.drawable.round_visibility_24
    } else {
        R.drawable.round_visibility_off_24
    }

    GuestLoginServiceDialogs(loginServiceStatus, dialogDismissHandler)

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
                value = loginFormData.nickname ?: "",
                onValueChange = { onFieldUpdate(FormFieldName.NICKNAME, it) },
                label = { Text(stringResource(id = FormFieldName.NICKNAME.labelId)) }
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
                value = loginFormData.password ?: "",
                onValueChange = { onFieldUpdate(FormFieldName.PASSWORD, it) },
                label = { Text(stringResource(FormFieldName.PASSWORD.labelId)) },
                trailingIcon = {
                    IconButton(
                        onClick = { onTrailingIconClicked(FormFieldName.PASSWORD) },
                    ) {
                        Icon(
                            painter = painterResource(id = authenticationCodeTrailingIcon),
                            contentDescription = ""
                        )
                    }
                },
                visualTransformation = if (loginFormData.isCodeHidden)
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
                value = loginFormData.tetraCubeHostAddress ?: "",
                onValueChange = {
                    onFieldUpdate(
                        FormFieldName.TETRACUBE_HOST_ADDRESS,
                        it
                    )
                },
                label = { Text(stringResource(FormFieldName.TETRACUBE_HOST_ADDRESS.labelId)) }
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = loginFormData.isFormValid,
            onClick = { onLoginButtonClicked() }
        ) {
            Text(stringResource(R.string.do_user_login))
        }
    }
}