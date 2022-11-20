package red.tetracube.tetracubeapp.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.registration.RegistrationScreenRoute

@Composable
fun LoginScreen(navHostController: NavHostController) {
    LoginScreenView(
        onEnrollmentButtonTap = { navHostController.navigate(RegistrationScreenRoute.route) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenView(
    onEnrollmentButtonTap: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        /* OutlinedTextField(
             singleLine = true,
             maxLines = 1,
             keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, autoCorrect = false),
             modifier = Modifier.fillMaxWidth(),
             value = loginFormData.username ?: "",
             onValueChange = { onFieldUpdateHandler(FormDataFieldName.USERNAME, it) },
             label = { Text(stringResource(id = FormDataFieldName.USERNAME.labelId)) }
         )
         OutlinedTextField(
             singleLine = true,
             maxLines = 1,
             keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, autoCorrect = false),
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 8.dp),
             value = loginFormData.password ?: "",
             onValueChange = { onFieldUpdateHandler(FormDataFieldName.PASSWORD, it) },
             label = { Text(stringResource(id = FormDataFieldName.PASSWORD.labelId)) }
         )
         OutlinedTextField(
             singleLine = true,
             maxLines = 1,
             keyboardOptions = KeyboardOptions(
                 imeAction = ImeAction.Next,
                 autoCorrect = false,
                 keyboardType = KeyboardType.Uri
             ),
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 8.dp),
             value = loginFormData.tetracubeHostAddress ?: "",
             onValueChange = { onFieldUpdateHandler(FormDataFieldName.TETRACUBE_HOST_ADDRESS, it) },
             label = { Text(stringResource(id = FormDataFieldName.TETRACUBE_HOST_ADDRESS.labelId)) }
         )

         Button(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 8.dp),
             enabled = loginFormData.isFormValid,
             onClick = { }
         ) {
             Text(stringResource(R.string.do_login))
         }

         TextButton(
             modifier = Modifier
                 .fillMaxWidth()
                 .padding(top = 16.dp),
             colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
             onClick = { navHostController.navigate(EnrollmentScreenRoute.route) }
         ) {
             Text(
                 text = stringResource(id = R.string.go_to_enrollment)
             )
         }*/

        Card() {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.secondary),
                onClick = { onEnrollmentButtonTap() }
            ) {
                Text(
                    text = stringResource(id = R.string.go_to_registration)
                )
            }
        }
    }
}