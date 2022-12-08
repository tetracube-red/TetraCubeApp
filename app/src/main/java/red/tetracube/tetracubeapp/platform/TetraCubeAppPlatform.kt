package red.tetracube.tetracubeapp.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import red.tetracube.tetracubeapp.R
import red.tetracube.tetracubeapp.core.routing.ScreenRoute
import red.tetracube.tetracubeapp.core.routing.tetracubeRedAppRoutes
import red.tetracube.tetracubeapp.core.settings.TetraCubeSettings
import red.tetracube.tetracubeapp.housedevicesmesh.HouseDevicesMeshRoute
import red.tetracube.tetracubeapp.splash.SplashScreenRoute
import red.tetracube.tetracubeapp.todo.ToDoRoute

@Composable
fun TetraCubeAppPlatform(tetraCubeAppPlatformViewModel: TetraCubeAppPlatformViewModel) {
    val navHostController = rememberNavController()
    val shouldShowBackIcon = navHostController.previousBackStackEntry != null
    val currentBackStack by navHostController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    val currentRoute = tetracubeRedAppRoutes
        .find { it.route == currentDestination?.route }
        ?: SplashScreenRoute

    val applicationSettings = tetraCubeAppPlatformViewModel.applicationSettings
    LaunchedEffect(
        key1 = true,
        block = {
            tetraCubeAppPlatformViewModel.loadApplicationData()
        }
    )

    TetraCubeAppPlatformView(
        navHostController,
        currentRoute,
        shouldShowBackIcon,
        applicationSettings,
        onNavigationBackTap = { navHostController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TetraCubeAppPlatformView(
    navHostController: NavHostController,
    currentRoute: ScreenRoute,
    shouldShowBackIcon: Boolean,
    applicationSettings: TetraCubeSettings,
    onNavigationBackTap: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentRoute.showAppBar) {
                TetracubeTopAppBar(
                    shouldShowBackIcon,
                    currentRoute.screenTitle,
                    onNavigationBackTap
                )
            }
        },
        content = { innerPadding ->
            TetraCubeAppNavigationHost(
                modifier = Modifier.padding(innerPadding),
                navHostController = navHostController,
                applicationSettings = applicationSettings
            )
        },
        bottomBar = {
            if (currentRoute.showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.round_emoji_objects_24),
                                contentDescription = stringResource(id = R.string.bottom_bar_house_devices_mesh)
                            )
                        },
                        label = { Text(stringResource(id = R.string.bottom_bar_house_devices_mesh)) },
                        selected = currentRoute is HouseDevicesMeshRoute,
                        onClick = {
                            navHostController.navigate(HouseDevicesMeshRoute.route) {
                                popUpTo(0)
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painterResource(id = R.drawable.round_checklist_24),
                                contentDescription = stringResource(id = R.string.bottom_bar_todo)
                            )
                        },
                        label = { Text(stringResource(id = R.string.bottom_bar_todo)) },
                        selected = currentRoute is ToDoRoute,
                        onClick = {
                            navHostController.navigate(ToDoRoute.route) {
                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TetracubeTopAppBar(
    shouldShowBackIcon: Boolean,
    title: String?,
    onNavigationBackTap: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title ?: stringResource(id = R.string.app_name)
            )
        },
        navigationIcon = {
            if (shouldShowBackIcon) {
                IconButton(
                    onClick = onNavigationBackTap,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_arrow_back_ios_24),
                        contentDescription = ""
                    )
                }
            }
        },
        actions = { }
    )
}