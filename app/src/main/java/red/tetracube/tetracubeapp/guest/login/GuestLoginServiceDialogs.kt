package red.tetracube.tetracubeapp.guest.login

import androidx.compose.runtime.Composable
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.definitions.ServiceCallStatus
import red.tetracube.tetracubeapp.ui.components.AlertDialogView
import red.tetracube.tetracubeapp.ui.components.LoaderOverlay

@Composable
fun GuestLoginServiceDialogs(
    guestLoginServiceStatus: ServiceCallStatus,
    dialogDismissHandler: (ServiceCallStatus) -> Unit
) {
    when (guestLoginServiceStatus) {
        ServiceCallStatus.CONNECTING -> {
            LoaderOverlay()
        }
        ServiceCallStatus.FINISHED_ERROR_CONNECTION -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.guest_login_connection_error_title,
                textStringId = R.string.guest_login_connection_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(guestLoginServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_SUCCESS -> {
            AlertDialogView(
                iconId = R.drawable.round_check_circle_outline_24,
                titleStringId = R.string.guest_login_success_title,
                textStringId = R.string.guest_login_success_message,
                confirmStringId = R.string.login_success,
                true,
                onDismiss = {
                    dialogDismissHandler(guestLoginServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_UNKNOWN -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.guest_login_unknown_error_title,
                textStringId = R.string.guest_login_unknown_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(guestLoginServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_TIMEOUT -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.guest_login_timeout_error_title,
                textStringId = R.string.guest_login_timeout_error_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(guestLoginServiceStatus)
                }
            )
        }
        ServiceCallStatus.FINISHED_ERROR_UNAUTHORIZED -> {
            AlertDialogView(
                iconId = R.drawable.round_highlight_off_24,
                titleStringId = R.string.guest_login_unauthorized_title,
                textStringId = R.string.guest_login_unauthorized_message,
                confirmStringId = R.string.registration_error_retry,
                true,
                onDismiss = {
                    dialogDismissHandler(guestLoginServiceStatus)
                }
            )
        }
        else -> {}
    }
}