package cz.mendelu.projek.navigation

sealed class Destination(
    val route: String
) {
    object LoginScreen: Destination(route = "login_screen")
    object RegisterScreen: Destination(route = "register_screen")
}