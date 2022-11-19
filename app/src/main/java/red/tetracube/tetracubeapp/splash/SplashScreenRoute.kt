package red.tetracube.tetracubeapp.splash

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object SplashScreenRoute : ScreenRoute {
    override val route: String = "splash"
    override val showAppBar: Boolean = false
    override val screenTitle: String? = null
    override val showBottomBar: Boolean = false
}