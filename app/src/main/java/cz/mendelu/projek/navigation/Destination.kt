package cz.mendelu.projek.navigation

sealed class Destination(
    val route: String
) {
    object LoginScreen: Destination(route = "login_screen")
    object RegisterScreen: Destination(route = "register_screen")
    object BoardsScreen: Destination(route = "boards_screen")
    object AddBoardScreen: Destination(route = "add_board_screen")
}