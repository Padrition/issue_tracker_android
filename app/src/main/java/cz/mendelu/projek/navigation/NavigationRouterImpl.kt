package cz.mendelu.projek.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun getNavController(): NavController {
        return navController
    }

    override fun navigateToLoginScreen() {
        navController.navigate(Destination.LoginScreen.route)
    }

    override fun navigateToRegisterScreen() {
        navController.navigate(Destination.RegisterScreen.route)
    }

    override fun navigateToBoardScreen() {
        navController.navigate(Destination.BoardsScreen.route)
    }

    override fun navigateToAddBoardScreen() {
        navController.navigate(Destination.AddBoardScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }

}