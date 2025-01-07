package cz.mendelu.projek.ui.screens.board_settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.constants.BAD_GATEWAY
import cz.mendelu.projek.constants.UNAUTHORIZED
import cz.mendelu.projek.ui.screens.board_screen.BoardScreenError
import cz.mendelu.projek.ui.screens.board_screen.BoardScreenUIState
import cz.mendelu.projek.ui.screens.boards_screen.BoardsScreenError
import cz.mendelu.projek.ui.screens.boards_screen.BoardsScreenUIState
import cz.mendelu.projek.utils.AuthHelper
import cz.mendelu.projek.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BoardSettingsViewModel @Inject constructor(
    private val repository: BoardRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
    private val authHelper: AuthHelper,
): ViewModel() {

    private val _uiState: MutableStateFlow<BoardSettingsScreenUIState> =
        MutableStateFlow(value = BoardSettingsScreenUIState.Idle)
    val uiState: StateFlow<BoardSettingsScreenUIState> get() = _uiState.asStateFlow()

    var data = BoardSettingsScreenData()

    fun deleteBoard(id: String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            _uiState.update {
                BoardSettingsScreenUIState.Loading
            }

            val result = withContext(Dispatchers.IO){
                repository.deleteBoard(
                    dataStoreManager.accessTokenFlow.first()!!,
                    id
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("BoardScreenViewModel", "Connection Error")
                    _uiState.update {
                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("BoardScreenViewModel", "Error : ${result.error}")
                    _uiState.update {
                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                    _uiState.update {
                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success: ${result.data}")
                    _uiState.update {
                        BoardSettingsScreenUIState.Deleted
                    }
                }
            }
        }
    }

    fun getBoard(id: String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            _uiState.update {
                BoardSettingsScreenUIState.Loading
            }

            val result = withContext(Dispatchers.IO){
                repository.getBoard(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    id = id,
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("BoardScreenViewModel", "Connection Error")
                    _uiState.update {
                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("BoardScreenViewModel", "Error : ${result.error}")
                    when(result.error.code){
                        UNAUTHORIZED -> {
                            authHelper.handleTokenException(
                                onRetry = {getBoard(id,retryCount +1)},
                                onError = { error ->
                                    _uiState.update {
                                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(error))
                                    }
                                }
                            )
                        }
                        else -> {
                            _uiState.update {
                                BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                    _uiState.update {
                        BoardSettingsScreenUIState.Error(BoardSettingScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success: ${result.data}")

                    data.board = result.data

                    _uiState.update {
                        BoardSettingsScreenUIState.Loaded(data)
                    }
                }
            }

        }
    }
}