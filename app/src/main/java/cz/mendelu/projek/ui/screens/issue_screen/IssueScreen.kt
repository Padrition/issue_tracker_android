package cz.mendelu.projek.ui.screens.issue_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.constants.Priorities
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.elements.DropDownCategoryMenu
import cz.mendelu.projek.ui.elements.DropDownPriorityMenu
import cz.mendelu.projek.ui.theme.MPLUSRounded1C

@Composable
fun IssueScreen(
    navigation: INavigationRouter,
    id: String?,
    boardId: String?,
){
    val viewModel = hiltViewModel<IssueScreenViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(IssueScreenData())
    }

    var editing by remember {
        mutableStateOf(false)
    }

    var nullError by remember {
        mutableStateOf(false)
    }

    state.value.let {
        when(it){
            is IssueScreenUIState.Error -> {}
            is IssueScreenUIState.Loaded -> {
                data = it.data
                if(data.board.categories != null){
                    data.selectedCategory = data.board.categories!!.first{ it.name == data.issue.status}
                }
            }
            IssueScreenUIState.Loading -> {
                if(id != null && boardId != null){
                    viewModel.loadData(id, boardId)
                }
            }
            is IssueScreenUIState.DataChanged -> {
                data = it.data
            }
        }
    }

    BaseScreen(
        topBarText = data.issue.title ?: stringResource(R.string.issue),
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if(editing){
                IconButton(
                    onClick = {
                        editing = false
                        viewModel.updateIssue()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = stringResource(R.string.description_save_issue)
                    )
                }
            }else{
                IconButton(
                    onClick = {
                        editing = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.description_edit_issue)
                    )
                }
            }
        }
    ) {
        if(editing){
            IssueScreenEditData(
                paddingValues = it,
                screenData = data,
                viewModel= viewModel,
                nullError = nullError,
            )
        }else{
            IssueScreenContent(
                paddingValues = it,
                screenData = data,
            )
        }
    }

}

@Composable
fun IssueScreenEditData(
    paddingValues: PaddingValues,
    screenData: IssueScreenData,
    viewModel: IssueScreenViewModel,
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
            value = screenData.issueEdit.title ?: "",
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
            value = screenData.issueEdit.description ?: "",
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
            selectedOption = screenData.issueEdit.priority ?: Priorities.MEDIUM,
            onOptionSelected = {
                viewModel.onPriorityChange(it)
            }
        )
    }

}

@Composable
fun IssueScreenContent(
    paddingValues: PaddingValues,
    screenData: IssueScreenData
){

    Column(
     modifier = Modifier
         .verticalScroll(rememberScrollState())
         .padding(paddingValues)
         .fillMaxSize()
         .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = screenData.issue.title ?: stringResource(R.string.issue_default_title),
            fontFamily = MPLUSRounded1C,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
            text = screenData.issue.description ?: stringResource(R.string.issue_default_description),
            fontFamily = MPLUSRounded1C,
            fontSize = 16.sp,
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column{
                Text(
                    text = stringResource(R.string.issue_status_chip),
                    fontWeight = FontWeight.SemiBold
                )
                SuggestionChip(
                    label = { Text(
                        text = screenData.issue.status ?: stringResource(R.string.issue_default_status)
                    ) },
                    onClick = {

                    },
                )
            }

            Column{
                Text(
                    text = stringResource(R.string.issue_priority_chip),
                    fontWeight = FontWeight.SemiBold
                )

                SuggestionChip(
                    label = { Text(
                        text = screenData.issue.priority ?: stringResource(R.string.issue_default_priority)
                    ) },
                    onClick = {

                    },
                )
            }
        }

    }

}