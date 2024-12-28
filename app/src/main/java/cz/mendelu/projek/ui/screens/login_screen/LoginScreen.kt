package cz.mendelu.projek.ui.screens.login_screen

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
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.progressindicator.CircularProgressIndicator
import cz.mendelu.projek.R
import cz.mendelu.projek.navigation.INavigationRouter
import cz.mendelu.projek.ui.elements.BaseScreen
import cz.mendelu.projek.ui.theme.MPLUSRounded1C

@Composable
fun LoginScreen(
    navigation: INavigationRouter
){
    Log.d("LoginScreen", "Entered Login Screen")

    val viewModel = hiltViewModel<LoginScreenViewModel>()

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    var data by remember {
        mutableStateOf(LoginScreenData())
    }

    var loading by remember {
        mutableStateOf(false)
    }

    loading = state.value == LoginScreenUIState.Loading

    var error by remember {
        mutableStateOf(false)
    }

    error = state.value is LoginScreenUIState.Error

    var errorMessage by remember {
        mutableStateOf("")
    }

    if (error){
        errorMessage = stringResource(id = (state.value as LoginScreenUIState.Error).error.communicationError)
    }

    state.value.let {
        when(it){
            LoginScreenUIState.Idle -> {

            }
            LoginScreenUIState.Loading -> {

            }
            is LoginScreenUIState.LoginCredentialsChanged -> {
                data = it.data
            }
            is LoginScreenUIState.Error -> {
            }
            is LoginScreenUIState.SignedIn -> {
                navigation.navigateToBoardScreen()
            }
        }
    }

    BaseScreen{
        LoginScreenContent(
            paddingValues = it,
            navigation = navigation,
            screenData = data,
            viewModel = viewModel,
            loading = loading,
            error = error,
            errorMessage = errorMessage,
        )
    }
}

@Composable
fun LoginScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    screenData: LoginScreenData,
    viewModel: LoginScreenViewModel,
    loading: Boolean,
    error: Boolean,
    errorMessage: String,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(Modifier.weight(0.2f))
            Text(
                text = stringResource(R.string.login_header),
                fontFamily = MPLUSRounded1C,
                fontWeight = FontWeight.Normal,
                fontSize = 64.sp,
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = screenData.signIn.email  ?: "",
                onValueChange = {
                    viewModel.onEmailChange(it)
                },
                isError = error,
                label = {
                    Text(stringResource(R.string.email))
                }
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = screenData.signIn.password ?: "",
                onValueChange = {
                    viewModel.onPasswordChange(it)
                },
                isError = error,
                label = {
                    Text(stringResource(R.string.password))
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
                enabled = screenData.signIn.email != null && screenData.signIn.password != null,
                onClick = {
                    viewModel.signIn()
                }
            ) {
                Text(stringResource(R.string.login))
            }

            Spacer(Modifier.weight(1.0f))

            Row {
                Text(
                    text = stringResource(R.string.create_an_account),
                    fontFamily = MPLUSRounded1C,
                    fontWeight = FontWeight.Normal,
                )
                Text(
                    modifier = Modifier
                        .clickable {
                            navigation.navigateToRegisterScreen()
                        },
                    text = stringResource(R.string.sign_up_button),
                    fontFamily = MPLUSRounded1C,
                    fontWeight = FontWeight.Bold,
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
