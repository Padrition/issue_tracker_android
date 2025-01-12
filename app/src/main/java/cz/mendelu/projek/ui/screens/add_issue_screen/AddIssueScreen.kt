package cz.mendelu.projek.ui.screens.add_issue_screen

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.analyzers.TextRecognizer
import cz.mendelu.projek.constants.Priorities
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.elements.CameraComposeView
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
//                LaunchedEffect(Unit) {
//
//                }
                navigation.returnBack()
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
    var showTextRecognition by remember { mutableStateOf(false) }

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

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            onClick = {
                showTextRecognition = true
            }
        ) {
            Text(stringResource(R.string.scan_text))
        }

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

    if(showTextRecognition){
        TextRecognitionOverlay(
            onTextRecognized = {recognizedText ->
                viewModel.onDescriptionChange(recognizedText)
                showTextRecognition = false
            },
            onCancel = {
                showTextRecognition = false
            }
        )
    }

}

@Composable
fun TextRecognitionOverlay(
    onTextRecognized: (String) -> Unit,
    onCancel: () -> Unit
) {
    var recognizedText by remember { mutableStateOf<String?>(null) }
    var recognitionActive by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
    ) {
        if(recognizedText == null){
            CameraComposeView(
                paddingValues = PaddingValues(0.dp),
                analyzer = if(recognitionActive) TextRecognizer { text ->
                    recognizedText = text
                    recognitionActive = false
                }else null
            )
        }else{
            ConfirmationDialog(
                text = recognizedText.orEmpty(),
                onConfirm = {
                    onTextRecognized(it)
                    recognizedText = null
                    recognitionActive = true
                },
                onCancel = {
                    recognizedText = null
                    recognitionActive = true
                }
            )
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            IconButton(onClick = { onCancel() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_text_recognition),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun ConfirmationDialog(
    text: String,
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.text_recognized),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = { onCancel() }) {
                    Text(stringResource(R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onConfirm(text) }) {
                    Text(stringResource(R.string.add_to_description))
                }
            }
        }
    }
}
