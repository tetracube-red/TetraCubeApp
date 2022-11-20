package red.tetracube.tetracubeapp.login

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object LoginScreenRoute : ScreenRoute {
    override val route: String = "login"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "Tetracube Login"
    override val showBottomBar: Boolean = false
}