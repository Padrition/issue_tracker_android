package cz.mendelu.projek.ui.screens.registration_screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun RegistrationScreen(
    navigation: INavigationRouter
){
    Log.d("RegistrationScreen", "Entered Registration Screen")

    val viewModel = hiltViewModel<RegistrationScreenViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(RegistrationScreenData())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    loading = state.value == RegistrationScreenUIState.Loading

    var passwordMismatch by remember {
        mutableStateOf(false)
    }

    passwordMismatch = viewModel.checkPasswordMismatch()

    var error by remember {
        mutableStateOf(false)
    }

    error = state.value is RegistrationScreenUIState.Error

    var errorMessage by remember {
        mutableStateOf("")
    }

    if (error){
        errorMessage = stringResource(id = (state.value as RegistrationScreenUIState.Error).error.communicationError)
    }

    state.value.let {
        when(it){
            is RegistrationScreenUIState.Error -> {}
            RegistrationScreenUIState.Idle -> {}
            RegistrationScreenUIState.Loading -> {
            }
            RegistrationScreenUIState.Registered -> {
                navigation.navigateToBoardScreen()
            }
            is RegistrationScreenUIState.RegistrationCredentialsChanged -> {
                data = it.data
            }
        }
    }

    BaseScreen{
        RegisterScreen(
            paddingValues = it,
            navigation = navigation,
            screenData = data,
            viewModel = viewModel,
            loading = loading,
            passwordMismatch = passwordMismatch,
            error = error,
            errorMessage = errorMessage,
        )
    }

}

@Composable
fun RegisterScreen(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    screenData: RegistrationScreenData,
    viewModel: RegistrationScreenViewModel,
    loading: Boolean,
    passwordMismatch: Boolean,
    error: Boolean,
    errorMessage: String,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.weight(0.2f))
            Text(
                text = stringResource(R.string.sign_up_header),
                fontFamily = MPLUSRounded1C,
                fontWeight = FontWeight.Normal,
                fontSize = 64.sp,
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = screenData.create.email ?: "",
                isError = error,
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                label = {
                    Text(stringResource(R.string.email))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = screenData.create.password ?: "",
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                isError = error || passwordMismatch,
                label = {
                    Text(stringResource(R.string.password))
                },
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = screenData.passwordCheck ?: "",
                onValueChange = {
                    viewModel.onPasswordCheckChange(it)
                },
                label = {
                    Text(stringResource(R.string.repeat_password))
                },
                isError = error || passwordMismatch,
                supportingText = {
                    if (passwordMismatch){
                        Text(
                            text = stringResource( R.string.password_mismatch),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation()
            )

            if(error){
                Text(
                    textAlign = TextAlign.Center,
                    text = errorMessage,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }

            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                enabled = !passwordMismatch && screenData.create.email != null,
                onClick = {
                    viewModel.register()
                }
            ){
                Text(stringResource(R.string.sign_up))
            }

            Spacer(Modifier.weight(1.0f))

            Row {
                Text(
                    modifier = Modifier
                        .clickable {
                            navigation.navigateToLoginScreen()
                        },
                    text = stringResource(R.string.login_button),
                    fontFamily = MPLUSRounded1C,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.if_already_registered),
                    fontFamily = MPLUSRounded1C,
                    fontWeight = FontWeight.Normal,
                )
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