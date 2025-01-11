package cz.mendelu.projek.ui.screens.add_issue_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import cz.mendelu.projek.constants.Priorities
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.elements.DropDownCategoryMenu
import cz.mendelu.projek.ui.elements.DropDownPriorityMenu

@Composable
fun AddIssueScreen(
    navigation: INavigationRouter,
    id: String?
){

    val viewModel = hiltViewModel<AddIssueViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(AddIssueScreenData())
    }

    var cannotBeNullError by remember {
        mutableStateOf(false)
    }

    cannotBeNullError = state.value == AddIssueScreenUIState.cannotBeNull

    state.value.let {
        when(it){
            AddIssueScreenUIState.Loading -> {
                if (id != null){
                    viewModel.loadBoard(id)
                }
            }
            is AddIssueScreenUIState.Error -> {}
            is AddIssueScreenUIState.Loaded -> {
                data = it.data
            }
            is AddIssueScreenUIState.DataChanged -> {
                data = it.data
            }
            AddIssueScreenUIState.Added -> {
                LaunchedEffect(null) {
                    navigation.returnBack()
                }
            }
            AddIssueScreenUIState.cannotBeNull -> {}
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.add_issue),
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(
                onClick = {
                    if(id != null){
                        viewModel.addIssue(id)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.description_add_issue)
                )
            }
        }
    ) {
        AddIssueScreenContent(
            paddingValues = it,
            screenData = data,
            viewModel = viewModel,
            nullError = cannotBeNullError
        )
    }


}

@Composable
fun AddIssueScreenContent(
    paddingValues: PaddingValues,
    screenData: AddIssueScreenData,
    viewModel: AddIssueViewModel,
    nullError: Boolean,
){

    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = screenData.issueCreate.title ?: "",
            onValueChange = {
                viewModel.onTitleChange(it)
            },
            isError = nullError,
            supportingText = {
                if(nullError){
                    Text(stringResource(R.string.error_title_cannot_be_empty))
                }
            },
            label = {
                Text(stringResource(R.string.issue_title))
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            value = screenData.issueCreate.description ?: "",
            onValueChange = {
                viewModel.onDescriptionChange(it)
            },
            isError = nullError,
            supportingText = {
                if(nullError){
                    Text(stringResource(R.string.error_description_cannot_be_empty))
                }
            },
            label = {
                Text(stringResource(R.string.issue_description))
            }
        )

        DropDownCategoryMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            options = screenData.board.categories ?: listOf(),
            selectedOption = screenData.selectedCategory,
            onOptionSelected = {
                viewModel.onCategoryChange(it)
            }
        )

        DropDownPriorityMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            options = Priorities.toList(),
            selectedOption = screenData.issueCreate.priority ?: Priorities.MEDIUM,
            onOptionSelected = {
                viewModel.onPriorityChange(it)
            }
        )
    }

}