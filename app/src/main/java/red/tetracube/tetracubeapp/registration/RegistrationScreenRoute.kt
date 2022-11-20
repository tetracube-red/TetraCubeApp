package red.tetracube.tetracubeapp.registration

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object RegistrationScreenRoute : ScreenRoute {
    override val route: String = "registration"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "Account Registration"
    override val showBottomBar: Boolean = false
}