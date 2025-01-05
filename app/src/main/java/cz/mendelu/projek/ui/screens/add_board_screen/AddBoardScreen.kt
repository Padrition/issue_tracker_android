package cz.mendelu.projek.ui.screens.add_board_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen

@Composable
fun AddBoardScreen(
    navigation: INavigationRouter,
) {

    val viewModel = hiltViewModel<AddBoardViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(AddBoardScreenData())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    loading = state.value == AddBoardScreenUIState.Loading

    state.value.let {
        when(it){
            is AddBoardScreenUIState.BoardDescriptionChanged -> {
                data = it.data
            }
            is AddBoardScreenUIState.BoardNameChanged -> {
                data = it.data
            }
            is AddBoardScreenUIState.Error -> {}
            AddBoardScreenUIState.Idle -> {}
            AddBoardScreenUIState.Loading -> {}
            AddBoardScreenUIState.Created -> {
                navigation.navigateToBoardScreen()
            }
        }
    }

    BaseScreen (
        topBarText = stringResource(R.string.new_board),
        onBackClick = {
            navigation.returnBack()
        }
    ){
        AddBoardScreenContent(
            paddingValues = it,
            viewModel = viewModel,
            screenData = data,
            loading = loading,
        )
    }

}

@Composable
fun AddBoardScreenContent(
    paddingValues: PaddingValues,
    viewModel: AddBoardViewModel,
    screenData: AddBoardScreenData,
    loading: Boolean,
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = screenData.boardCreate.name ?: "",
                onValueChange = {
                    viewModel.onNameChange(it)
                },
                supportingText = {
                    if(screenData.boardCreate.name.isNullOrBlank()){
                        Text(
                            modifier = Modifier.padding(0.dp),
                            text = stringResource(R.string.new_board_board_name_is_empty),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                },
                label = {
                    Text(stringResource(R.string.board_name_label))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = screenData.boardCreate.description ?: "",
                onValueChange = {
                    viewModel.onDescriptionChange(it)
                },
                label = {
                    Text(stringResource(R.string.board_description_label))
                }
            )

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                enabled = screenData.boardCreate.name != null,
                onClick = {
                    viewModel.createBoard()
                }
            ){
                Text(stringResource(R.string.create_new_board))
            }
        }

        if(loading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background.copy(alpha = 0.8f)
                    ),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
    }

}