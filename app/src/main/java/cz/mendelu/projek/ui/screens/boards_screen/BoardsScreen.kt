package cz.mendelu.projek.ui.screens.boards_screen

import android.util.Log
import android.widget.ImageButton
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.Destination
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.elements.BoardItem
import cz.mendelu.projek.ui.elements.PlaceHolderScreen
import cz.mendelu.projek.ui.elements.PlaceholderScreenContent

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
            is BoardScreenUIState.Error -> {}
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.boards_screen_top_bar_text),
        actions = {
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.description_settings)
                )
            }
        },
        placeholderScreenContent = if (state.value is BoardScreenUIState.Error){
            PlaceholderScreenContent(
                image = R.drawable.communicationerror,
                title = null,
                text = stringResource(id = (state.value as BoardScreenUIState.Error).error.communicationError)
            )
        } else null,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigation.navigateToAddBoardScreen()
                },
                icon = { Icon(Icons.Filled.Add, stringResource(R.string.description_add_board)) },
                text = { Text(stringResource(R.string.new_board)) }
            )
        }
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
    if (screenData.boards.isEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Column{
                Text(
                    text = stringResource(R.string.no_boards_yet),
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }else{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            screenData.boards.forEach { board ->
                item {
                    BoardItem(board)
                }
            }
        }
    }
}