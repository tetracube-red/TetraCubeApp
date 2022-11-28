package red.tetracube.tetracubeapp.core.routing

import red.tetracube.tetracubeapp.codescanner.CameraQRScannerScreenRoute
import red.tetracube.tetracubeapp.login.LoginScreenRoute
import red.tetracube.tetracubeapp.registration.RegistrationScreenRoute
import red.tetracube.tetracubeapp.splash.SplashScreenRoute

interface ScreenRoute {
    val route: String
    val showAppBar: Boolean
    val screenTitle: String?
    val showBottomBar: Boolean
}

val tetracubeRedAppRoutes = listOf(
    SplashScreenRoute,
    LoginScreenRoute,
    RegistrationScreenRoute,
    CameraQRScannerScreenRoute
    //  SmartHomeScreen
)