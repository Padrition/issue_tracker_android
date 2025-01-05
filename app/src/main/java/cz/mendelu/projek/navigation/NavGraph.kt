package cz.mendelu.projek.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.projek.ui.screens.add_board_screen.AddBoardScreen
import cz.mendelu.projek.ui.screens.board_screen.BoardScreen
import cz.mendelu.projek.ui.screens.boards_screen.BoardsScreen
import cz.mendelu.projek.ui.screens.login_screen.LoginScreen
import cz.mendelu.projek.ui.screens.registration_screen.RegistrationScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph (
    navController: NavHostController = rememberNavController(),
    navigation: INavigationRouter = remember { NavigationRouterImpl(navController) },
    startDestination: String
){
    NavHost(
        navController= navController,
        startDestination = startDestination
    ){
        composable(Destination.RegisterScreen.route){
            RegistrationScreen(navigation)
        }

        composable(Destination.LoginScreen.route){
            LoginScreen(navigation)
        }

        composable(Destination.BoardsScreen.route){
            BoardsScreen(navigation)
        }

        composable(Destination.AddBoardScreen.route){
            AddBoardScreen(navigation)
        }

        composable(Destination.BoardScreen.route + "/{id}",
                arguments = listOf(
                    navArgument("id"){
                        type = NavType.StringType
                        defaultValue = "0"
                    }
                )
            ){
            val id = it.arguments?.getString("id")
            BoardScreen(navigation, id)
        }
    }
}