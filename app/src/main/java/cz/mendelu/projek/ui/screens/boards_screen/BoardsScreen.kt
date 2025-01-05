package cz.mendelu.projek.ui.screens.boards_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.Destination
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen

@Composable
fun BoardsScreen(
    navigation: INavigationRouter
){
    Log.d("BoardsScreen", "Entered Boards Screen")

    val viewModel = hiltViewModel<BoardScreenViewModel>()

    BaseScreen(
        topBarText = stringResource(R.string.boards_screen_top_bar_text)
    ) {
        BoardsScreenContent(
            navigation = navigation,
            paddingValues = it,
        )
    }
}

@Composable
fun BoardsScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

    }
}