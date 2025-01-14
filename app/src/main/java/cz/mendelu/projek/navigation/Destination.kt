package cz.mendelu.projek.navigation

sealed class Destination(
    val route: String
) {
    object LoginScreen: Destination(route = "login_screen")
    object RegisterScreen: Destination(route = "register_screen")
    object BoardsScreen: Destination(route = "boards_screen")
    object AddBoardScreen: Destination(route = "add_board_screen")
    object BoardScreen: Destination(route = "board_screen")
    object BoardSettingsScreen: Destination(route = "board_settings_screen")
    object AddIssueScreen: Destination(route = "add_issue_screen")
    object IssueScreen: Destination(route = "issue_screen")
    object SettingsScreen: Destination(route = "settings_screen")
}