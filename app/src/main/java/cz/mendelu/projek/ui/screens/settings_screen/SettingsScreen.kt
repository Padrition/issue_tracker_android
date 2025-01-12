package cz.mendelu.projek.ui.screens.settings_screen

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.projek.R
import cz.mendelu.projek.constants.Languages
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen

@Composable
fun SettingsScreen(
    navigation: INavigationRouter
){

    val viewModel = hiltViewModel<SettingsScreenViewModel>()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.languages.collectAsState()

    state.value.let {
        when(it){
            SettingsScreenUIState.Idle -> {}
            SettingsScreenUIState.LoggedOut -> {
                navigation.navigateToLoginScreen()
            }
        }
    }

    val context = LocalContext.current

    BaseScreen (
        topBarText = stringResource(R.string.settings_title),
        onBackClick = { navigation.returnBack() },
        actions = {
            IconButton(
                onClick = {viewModel.logOut()}
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.logOut)

                )
            }
        }
    ){
        SettingsScreenContent(
            paddingValues = it,
            context = context,
            selectedLanguage = selectedLanguage,
            onLanguageChange = {
                viewModel.updateLanguage(it)
            }
        )
    }
}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    context: Context,
    selectedLanguage: Languages,
    onLanguageChange: (Languages) -> Unit
){
    var expanded by remember { mutableStateOf(false)}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ){

        TextButton(onClick = { expanded = true }) {
            Text(text = "${context.getString(R.string.current_language)}: ${selectedLanguage.name}")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                onLanguageChange(Languages.CZ)
                expanded = false
            },
                text = {
                    Text(context.getString(R.string.language_cz))
                })
            DropdownMenuItem(onClick = {
                onLanguageChange(Languages.EN)
                expanded = false
            },
                text = {
                    Text(context.getString(R.string.language_eng))
                })
        }
    }
}