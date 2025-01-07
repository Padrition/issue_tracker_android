package cz.mendelu.projek.ui.screens.board_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen

@Composable
fun BoardSettingsScreen(
    navigation: INavigationRouter,
    id: String?
){

    val viewModel = hiltViewModel<BoardSettingsViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(BoardSettingsScreenData())
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    state.value.let {
        when(it){
            is BoardSettingsScreenUIState.Error -> {}
            BoardSettingsScreenUIState.Idle -> {
                if(id != null) {
                    viewModel.getBoard(id)
                }
            }
            is BoardSettingsScreenUIState.Loaded -> {
                data = it.data
            }
            BoardSettingsScreenUIState.Loading -> {}
            BoardSettingsScreenUIState.Deleted -> {
                navigation.navigateToBoardScreen()
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.baord_settings),
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(
                onClick = {
                    showDialog = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.description_delete_board)
                )
            }
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = stringResource(R.string.description_save_baord_chenges)
                )
            }
        }
    ) {
        BoardSettingsContent(
            paddingValues = it,
            showDialog = showDialog,
            onDialogDismiss = {showDialog = false},
            onClick = {
                if (id != null){
                    viewModel.deleteBoard(
                        id
                    )
                }
            },
        )
    }

}

@Composable
fun BoardSettingsContent(
    paddingValues: PaddingValues,
    showDialog: Boolean,
    onDialogDismiss: () -> Unit,
    onClick: () -> Unit,
){

    if(showDialog){
        DeleteDialog(
            onDismissRequest = {onDialogDismiss()},
            onClick = {onClick()}
        )
    }

}

@Composable
fun DeleteDialog(
    onDismissRequest: () -> Unit,
    onClick: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.delete_board_confirm),
                        modifier = Modifier.padding(8.dp),
                        textAlign = TextAlign.Center,
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier.padding(8.dp),
                            onClick = onClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = Color.White,
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.delete_board_button),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Button(
                            modifier = Modifier.padding(8.dp),
                            onClick = onDismissRequest,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White,
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.cancel),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}
