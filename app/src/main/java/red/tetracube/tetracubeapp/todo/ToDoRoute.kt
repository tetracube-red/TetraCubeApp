package red.tetracube.tetracubeapp.todo

import red.tetracube.tetracubeapp.core.routing.ScreenRoute

object ToDoRoute : ScreenRoute {
    override val route: String = "to-do"
    override val showAppBar: Boolean = true
    override val screenTitle: String = "ToDo"
    override val showBottomBar: Boolean = true
}