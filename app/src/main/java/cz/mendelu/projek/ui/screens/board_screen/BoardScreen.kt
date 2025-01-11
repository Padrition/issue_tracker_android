package cz.mendelu.projek.ui.screens.board_screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.elements.CategoryBoard

@Composable
fun BoardScreen(
    navigation: INavigationRouter,
    id: String?
){

    val viewModel = hiltViewModel<BoardScreenViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(BoardScreenData())
    }

    state.value.let {
        when(it){
            is BoardScreenUIState.Error -> {}
            BoardScreenUIState.Idle -> {
                if (id != null){
                    viewModel.getBoard(id)
                }
            }
            is BoardScreenUIState.Loaded -> {
                data = it.data
            }
            BoardScreenUIState.Loading -> {}
        }
    }

    BaseScreen (
        topBarText = data.board.name ?: stringResource(R.string.board),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (id != null){
                        navigation.navigateToAddIssueScreen(
                            id
                        )
                    }
                },
                icon =  { Icon(Icons.Filled.Edit, stringResource(R.string.add_issue)) },
                text = { Text(stringResource(R.string.add_issue)) },
            )
        },
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(
                onClick = {
                    navigation.navigateToBoardSettingsScreen(id!!)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.description_board_settings)
                )
            }
        }
    ){
        BoardScreenContent(
            navigation = navigation,
            paddingValues = it,
            viewModel = viewModel,
            screenData = data
        )
    }

}

@Composable
fun BoardScreenContent(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: BoardScreenViewModel,
    screenData: BoardScreenData,
){

    if (screenData.board.categories.isNullOrEmpty()){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ){
            Column{
                Text(
                    text = stringResource(R.string.no_categories_yet),
                    fontWeight = FontWeight.Light,
                )
            }
        }
    }else{

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            screenData.board.categories!!.forEach { category ->
                item {
                    CategoryBoard(
                        category = category,
                        issues = screenData.issues.filter { it.status == category.name },
                        onIssueClick = {

                        },
                    )
                }
            }
        }
    }
}