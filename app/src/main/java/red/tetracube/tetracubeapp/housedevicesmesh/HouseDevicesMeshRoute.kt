package red.tetracube.tetracubeapp.housedevicesmesh

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object HouseDevicesMeshRoute : ScreenRoute {
    override val route: String = "house/devices/mesh"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "House Devices Mesh"
    override val showBottomBar: Boolean = true
}