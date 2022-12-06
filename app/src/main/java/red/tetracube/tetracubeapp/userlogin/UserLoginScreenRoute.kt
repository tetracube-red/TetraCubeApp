package red.tetracube.tetracubeapp.userlogin

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object UserLoginScreenRoute : ScreenRoute {
    override val route: String = "user/login"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "User Login"
    override val showBottomBar: Boolean = false
}