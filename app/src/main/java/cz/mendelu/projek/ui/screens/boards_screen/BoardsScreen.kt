package cz.mendelu.projek.ui.screens.boards_screen

import android.util.Log
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(BoardScreenData())
    }

    state.value.let {
        when(it){
            is BoardScreenUIState.Loaded -> {
                data = it.data
            }
            BoardScreenUIState.Loading -> {}
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.boards_screen_top_bar_text)
    ) {
        BoardsScreenContent(
            navigation = navigation,
            paddingValues = it,
            screenData = data,
        )
    }
}

@Composable
fun BoardsScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    screenData: BoardScreenData,
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        screenData.boards.forEach { board ->
            item {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(board.name ?: "No name")
                    Text(board.description ?: "No description")
                }
            }
        }
    }
}