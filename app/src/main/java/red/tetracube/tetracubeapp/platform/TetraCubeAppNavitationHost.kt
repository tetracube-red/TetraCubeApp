package red.tetracube.tetracubeapp.platform

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.housedevicesmesh.HouseDevicesMeshRoute
import red.tetracube.tetracubeapp.userlogin.UserLoginScreen
import red.tetracube.tetracubeapp.userlogin.UserLoginScreenRoute
import red.tetracube.tetracubeapp.splash.SplashScreen
import red.tetracube.tetracubeapp.splash.SplashScreenRoute
import red.tetracube.tetracubeapp.todo.ToDoRoute

@Composable
fun TetraCubeAppNavigationHost(
    modifier: Modifier,
    navHostController: NavHostController,
    applicationSettings: TetraCubeSettings,
) {
    NavHost(
        navController = navHostController,
        startDestination = SplashScreenRoute.route,
        modifier = modifier
    ) {
        composable(route = SplashScreenRoute.route) {
            SplashScreen(
                applicationSettings = applicationSettings,
                navHostController = navHostController
            )
        }
        composable(route = UserLoginScreenRoute.route) {
            UserLoginScreen(
                navHostController = navHostController
            )
        }
        composable(route = HouseDevicesMeshRoute.route) {
            Text("FeaturesMesh")
        }
        composable(route = ToDoRoute.route) {
            Text("ToDo")
        }
    }
}