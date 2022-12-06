package red.tetracube.tetracubeapp.core.routing

import red.tetracube.tetracubeapp.userlogin.UserLoginScreenRoute
import red.tetracube.tetracubeapp.splash.SplashScreenRoute

interface ScreenRoute {
    val route: String
    val showAppBar: Boolean
    val screenTitle: String?
    val showBottomBar: Boolean
}

val tetracubeRedAppRoutes = listOf(
    SplashScreenRoute,
    UserLoginScreenRoute
    //  SmartHomeScreen
)