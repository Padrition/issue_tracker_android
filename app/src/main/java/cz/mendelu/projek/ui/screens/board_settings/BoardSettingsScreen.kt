package cz.mendelu.projek.ui.screens.board_settings

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.constants.CategoryColors
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.utils.parseColor

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

    var memberIsPresentError by remember {
        mutableStateOf(false)
    }

    memberIsPresentError = state.value is BoardSettingsScreenUIState.MemberIsPresent

    var notAnEmail by remember {
        mutableStateOf(false)
    }

    notAnEmail = state.value is BoardSettingsScreenUIState.NotAValidEmail

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
            is BoardSettingsScreenUIState.onChage -> {
                data = it.data
            }
            BoardSettingsScreenUIState.MemberIsPresent -> {
            }
            BoardSettingsScreenUIState.NotAValidEmail -> {
            }
            BoardSettingsScreenUIState.Updated -> {
//                LaunchedEffect(null) {
//
//                }
                navigation.returnBack()
            }
        }
    }

    BaseScreen(
        topBarText = stringResource(R.string.baord_settings),
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            if(data.board.isCreator == true){
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
            }

            IconButton(
                onClick = {
                    viewModel.updateBoard()
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
            viewModel = viewModel,
            showDeleteDialog = showDialog,
            onDialogDismiss = {showDialog = false},
            onClick = {
                if (id != null){
                    viewModel.deleteBoard()
                }
            },
            screenData = data,
            memberIsPresent = memberIsPresentError,
            notAnEmail = notAnEmail,
        )
    }

}

@Composable
fun BoardSettingsContent(
    paddingValues: PaddingValues,
    viewModel: BoardSettingsViewModel,
    showDeleteDialog: Boolean,
    onDialogDismiss: () -> Unit,
    onClick: () -> Unit,
    screenData: BoardSettingsScreenData,
    memberIsPresent: Boolean,
    notAnEmail: Boolean,
){
    var selectedCategoryIndex by remember { mutableIntStateOf(-1) }
    var isColorDialogVisible by remember { mutableStateOf(false) }

    var newMember by remember { mutableStateOf("") }
    var newMemberEmpty by remember { mutableStateOf(false) }

    if(showDeleteDialog){
        DeleteDialog(
            onDismissRequest = {onDialogDismiss()},
            onClick = {onClick()}
        )
    }

    if(isColorDialogVisible){
        ColorDialog(
            onDismissRequest = {isColorDialogVisible = false},
            onColorSelected = { selectedColor ->
                if (selectedCategoryIndex >= 0){
                    Log.d("BoardSettingScreen" ,"color: ${selectedColor}")
                    viewModel.onSectionColorChange(selectedCategoryIndex, selectedColor)
                }
                isColorDialogVisible = false
            }
        )
    }

    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(paddingValues)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ){
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = screenData.board.name ?: "No name",
            onValueChange = {
                viewModel.onNameChange(it)
            },
            supportingText = {

            },
            label = {
                Text(stringResource(R.string.board_name_label))
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
                //.padding(16.dp),
            value = screenData.board.description ?: "No description",
            onValueChange = {
                viewModel.onDescriptionChange(it)
            },
            supportingText = {

            },
            label = {
                Text(stringResource(R.string.board_description_label))
            }
        )

        if (screenData.board.categories != null){
            screenData.board.categories!!.forEachIndexed { index, category ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ){
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = category.name ?: "",
                        onValueChange = {
                            viewModel.onSectionNameChange(index, it)
                        },
                        label = {
                            Text(stringResource(R.string.section_name))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = parseColor(category.color ?: "#123456"),
                            unfocusedBorderColor = parseColor(category.color ?: "#123456"),
                        ),
                        leadingIcon = {
                            IconButton(
                                onClick = {
                                    selectedCategoryIndex = index
                                    isColorDialogVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = stringResource(R.string.description_section_color),
                                    tint = parseColor(category.color ?: "#123456")
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.onSectionDelete(index)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.description_clear_section)
                                )
                            }
                        },
                        supportingText = {
                            if(index == 0){
                                Text(stringResource(R.string.color_hint))
                            }
                        }
                    )

                }
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = {
                viewModel.addSection()
            }
        ) {
            Text(stringResource(R.string.add_section))
        }

        if(screenData.board.members != null){
            Column (
                modifier = Modifier
                    .padding(8.dp)
            ) {
                screenData.board.members!!.forEachIndexed { index, member ->
                    InputChip(
                        modifier = Modifier
                            .padding(top = 4.dp),
                        onClick = {},
                        label = { Text(member) },
                        selected = true,
                        avatar = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = stringResource(R.string.description_avatar),
                                Modifier.size(InputChipDefaults.AvatarSize),
                            )
                        },
                        trailingIcon = {
                            if (member != screenData.board.createdBy){
                                IconButton(
                                    onClick = {
                                        viewModel.deleteMember(index)
                                    },
                                    modifier = Modifier
                                        .size(InputChipDefaults.AvatarSize)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Delete,
                                        contentDescription = stringResource(R.string.description_clear_member),
                                        Modifier.size(InputChipDefaults.AvatarSize)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = newMember,
            onValueChange = {
                newMemberEmpty = false
                newMember = it
            },
            supportingText = {
                if(memberIsPresent){
                    Text(stringResource(R.string.member_exists_error))
                }
                if(notAnEmail){
                    Text(stringResource(R.string.not_an_email_error))
                }
            },
            isError = newMemberEmpty || memberIsPresent || notAnEmail,
            label = {
                Text(stringResource(R.string.invite_member_lable))
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = {
                if(newMember.isNotEmpty()){
                    viewModel.inviteMember(newMember)
                    newMember = ""
                }else{
                    newMemberEmpty = true
                }
            }
        ) {
            Text(stringResource(R.string.invite_member))
        }


    }

}

@Composable
fun ColorDialog(
    onDismissRequest: () -> Unit,
    onColorSelected: (String) -> Unit
) {
    val colors = listOf(
        CategoryColors.BLUE to stringResource(R.string.blue),
        CategoryColors.GREEN to stringResource(R.string.green),
        CategoryColors.YELLOW to stringResource(R.string.yellow),
        CategoryColors.ORANGE to stringResource(R.string.orange),
        CategoryColors.RED to stringResource(R.string.red)
    )

    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select a Color",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    colors.forEach { (colorHex, label) ->
                        ColorCircle(
                            color = Color(android.graphics.Color.parseColor(colorHex)),
                            label = label,
                            onClick = {
                                onColorSelected(colorHex)
                                onDismissRequest()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ColorCircle(
    color: Color,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
                .clickable(onClick = onClick)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, style = MaterialTheme.typography.bodySmall)
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
