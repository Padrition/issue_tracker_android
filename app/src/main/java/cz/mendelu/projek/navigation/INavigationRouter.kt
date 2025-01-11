package cz.mendelu.projek.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun getNavController(): NavController
    fun navigateToLoginScreen()
    fun navigateToRegisterScreen()
    fun navigateToBoardScreen()
    fun navigateToAddBoardScreen()
    fun navigateToSingleBoardScreen(boardId: String)
    fun navigateToBoardSettingsScreen(boardId: String)
    fun navigateToAddIssueScreen(boardId: String)
    fun navigateToIssueScreen(issueId: String, boardId: String)
    fun returnBack()
}