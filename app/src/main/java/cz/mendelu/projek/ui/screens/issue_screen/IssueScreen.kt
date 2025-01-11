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
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.theme.MPLUSRounded1C

@Composable
fun IssueScreen(
    navigation: INavigationRouter,
    id: String?
){
    val viewModel = hiltViewModel<IssueScreenViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(IssueScreenData())
    }

    state.value.let {
        when(it){
            is IssueScreenUIState.Error -> {}
            is IssueScreenUIState.Loaded -> {
                data = it.data
            }
            IssueScreenUIState.Loading -> {
                if(id != null){
                    viewModel.loadIssue(id)
                }
            }
        }
    }

    BaseScreen(
        topBarText = data.issue.title ?: stringResource(R.string.issue),
        onBackClick = {
            navigation.returnBack()
        },
    ) {
        IssueScreenContent(
            paddingValues = it,
            screenData = data,
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