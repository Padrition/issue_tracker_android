package cz.mendelu.projek.ui.screens.registration_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.auth.AuthRemoteRepositoryImpl
import cz.mendelu.projek.communication.auth.SignIn
import cz.mendelu.projek.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val repository: AuthRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {


    private val _uiState : MutableStateFlow<RegistrationScreenUIState> =
        MutableStateFlow(value = RegistrationScreenUIState.Idle)
    val uiState: StateFlow<RegistrationScreenUIState> get() = _uiState.asStateFlow()

    var data = RegisterScreenData()

    fun onEmailChange(email: String?){
        data.create.email = email
        data.create.login = email

        _uiState.update {
            RegistrationScreenUIState.RegistrationCredentialsChanged(data)
        }
    }

    fun onPasswordChange(password: String?){
        data.create.password = password

        _uiState.update {
            RegistrationScreenUIState.RegistrationCredentialsChanged(data)
        }
    }

    fun onPasswordCheckChange(password: String?){
        data.passwordCheck = password

        _uiState.update {
            RegistrationScreenUIState.RegistrationCredentialsChanged(data)
        }
    }

    fun checkPasswordMismatch(): Boolean{
        return (data.create.password != data.passwordCheck)
    }

    fun register(){
        _uiState.update {
            RegistrationScreenUIState.Loading
        }
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO){
                repository.create(data.create)
            }
            when(response){
                is CommunicationResult.ConnectionError -> {
                    Log.d("RegistrationScreenViewModel", "Connection error")
                    _uiState.update {
                        RegistrationScreenUIState.Error(RegistrationScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("RegistrationScreenViewModel", "Error : ${response.error}")
                    _uiState.update {
                        RegistrationScreenUIState.Error(RegistrationScreenError(R.string.error))
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("RegistrationScreenViewModel", "Exception: ${response.exception}")
                    _uiState.update {
                        RegistrationScreenUIState.Error(RegistrationScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("RegistrationScreenViewModel", "Success: ${response.data}")

                    val signinResponse = withContext(Dispatchers.IO){
                        val signIn = SignIn(
                            email = data.create.email,
                            password = data.create.password
                        )
                        repository.signIn(signIn)
                    }
                    when(signinResponse){
                        is CommunicationResult.ConnectionError -> {
                            Log.d("RegistrationScreenViewModel", "Connection error")
                            _uiState.update {
                                RegistrationScreenUIState.Error(RegistrationScreenError(R.string.connectionError))
                            }
                        }
                        is CommunicationResult.Error -> {
                            Log.d("RegistrationScreenViewModel", "Error : ${signinResponse.error}")
                            _uiState.update {
                                RegistrationScreenUIState.Error(RegistrationScreenError(R.string.error))
                            }
                        }
                        is CommunicationResult.Exception -> {
                            Log.d("RegistrationScreenViewModel", "Exception: ${signinResponse.exception}")
                            _uiState.update {
                                RegistrationScreenUIState.Error(RegistrationScreenError(R.string.exception))
                            }
                        }
                        is CommunicationResult.Success -> {
                            Log.d("RegistrationScreenViewModel", "Success: ${signinResponse.data}")
                            if (signinResponse.data.accessToken != null && signinResponse.data.refreshToken != null){
                                dataStoreManager.updateTokens(signinResponse.data.accessToken!!, signinResponse.data.refreshToken!!)
                            }
                            _uiState.update {
                                RegistrationScreenUIState.Registered
                            }
                        }
                    }
                }
            }
        }
    }
}