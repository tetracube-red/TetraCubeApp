package red.tetracube.tetracubeapp.platform

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.guest.login.GuestLoginScreen
import red.tetracube.tetracubeapp.guest.login.GuestLoginScreenRoute
import red.tetracube.tetracubeapp.housedevicesmesh.HouseDevicesMeshRoute
import red.tetracube.tetracubeapp.housedevicesmesh.HouseDevicesMeshScreen
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
        composable(route = GuestLoginScreenRoute.route) {
            GuestLoginScreen(
                navHostController = navHostController
            )
        }
        composable(route = HouseDevicesMeshRoute.route) {
            val tetraCube = applicationSettings.pairedTetracubesList.find { t -> t.isDefault }
                ?: applicationSettings.pairedTetracubesList.first()
            HouseDevicesMeshScreen(
                tetraCube = tetraCube,
            )
        }
        composable(route = ToDoRoute.route) {
            Text("ToDo")
        }
    }
}