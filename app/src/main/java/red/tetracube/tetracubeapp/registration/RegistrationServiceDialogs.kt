package red.tetracube.tetracubeapp.registration

import androidx.compose.runtime.Composable
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.ui.components.AlertDialogView
import red.tetracube.tetracubeapp.ui.components.LoaderOverlay

@Composable
fun RegistrationServiceDialogs(
    registrationServiceStatus: ServiceCallStatus,
    dialogDismissHandler: (ServiceCallStatus) -> Unit
) {
    when (registrationServiceStatus) {
        ServiceCallStatus.CONNECTING -> {
            LoaderOverlay()
        }
        ServiceCallStatus.FINISHED_ERROR_CONNECTION -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.account_registration_connection_error_title,
                textStringId = R.string.account_registration_connection_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_SUCCESS -> {
            AlertDialogView(
                iconId = R.drawable.round_check_circle_outline_24,
                titleStringId = R.string.account_registration_success_title,
                textStringId = R.string.account_registration_success_message,
                confirmStringId = R.string.registration_success,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_UNKNOWN -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.account_registration_unknown_error_title,
                textStringId = R.string.account_registration_unknown_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_TIMEOUT -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.account_registration_timeout_error_title,
                textStringId = R.string.account_registration_timeout_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_NOT_FOUND -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.account_registration_not_found_error_title,
                textStringId = R.string.account_registration_not_found_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_BAD_REQUEST -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.account_registration_invalid_request_error_title,
                textStringId = R.string.account_registration_invalid_request_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(registrationServiceStatus)
                }
            )
        }
        else -> {}
    }
}