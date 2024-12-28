package cz.mendelu.projek.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToLoginScreen()
    fun navigateToRegisterScreen()
    fun navigateToBoardScreen()
    fun returnBack()
}