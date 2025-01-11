package cz.mendelu.projek.ui.screens.add_issue_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen

@Composable
fun AddIssueScreen(
    navigation: INavigationRouter,
    id: String?
){

    BaseScreen(
        topBarText = stringResource(R.string.add_issue),
        onBackClick = {
            navigation.returnBack()
        },
    ) {
        AddIssueScreenContent(

        )
    }


}

@Composable
fun AddIssueScreenContent(){

}