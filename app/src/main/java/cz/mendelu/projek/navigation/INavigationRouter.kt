package cz.mendelu.projek.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToLoginScreen()
    fun navigateToRegisterScreen()
    fun navigateToBoardScreen()
    fun navigateToAddBoardScreen()
    fun navigateToSingleBoardScreen(boardId: String)
    fun returnBack()
}