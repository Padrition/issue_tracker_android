package cz.mendelu.projek.ui.screens.login_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.auth.AuthRemoteRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(private val repository: AuthRemoteRepositoryImpl): ViewModel() {

    private val _uiState: MutableStateFlow<LoginScreenUIState> =
        MutableStateFlow(value = LoginScreenUIState.Idle)
    val uiState: StateFlow<LoginScreenUIState> get() = _uiState.asStateFlow()

    var data = LoginScreenData()

    fun onEmailChange(email: String?){
        data.signIn.email = email

        _uiState.update {
            LoginScreenUIState.LoginCredentialsChanged(data)
        }
    }

    fun onPasswordChange(password: String?){
        data.signIn.password = password

        _uiState.update {
            LoginScreenUIState.LoginCredentialsChanged(data)
        }
    }

    fun signIn(){
        _uiState.update {
            LoginScreenUIState.Loading
        }
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.signIn(data.signIn)
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("LoginScreenViewModel", "Connection error")
                    _uiState.update {
                        LoginScreenUIState.Error(LoginScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("LoginScreenViewModel", "Error ${result.error}")
                    _uiState.update {
                        LoginScreenUIState.Error(LoginScreenError(R.string.error))
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("LoginScreenViewModel", "Exception ${result.exception}")
                    _uiState.update {
                        LoginScreenUIState.Error(LoginScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("LoginScreenViewModel", "Success ${result.data}")
                }
            }
        }
    }

}