package red.tetracube.tetracubeapp.guest.login

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object GuestLoginScreenRoute : ScreenRoute {
    override val route: String = "guest/login"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "Guest Login"
    override val showBottomBar: Boolean = false
}