package cz.mendelu.projek.ui.screens.board_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.communication.issue.IssueRemoteRepositoryImpl
import cz.mendelu.projek.constants.UNAUTHORIZED
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
class BoardScreenViewModel @Inject constructor(
    private val repository: BoardRemoteRepositoryImpl,
    private val issueRepository: IssueRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
    private val authHelper: AuthHelper,
): ViewModel() {

    private val _uiState: MutableStateFlow<BoardScreenUIState> =
        MutableStateFlow(value = BoardScreenUIState.Idle)
    val uiState: StateFlow<BoardScreenUIState> get() = _uiState.asStateFlow()

    var data = BoardScreenData()

    private fun getIssues(id:String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                BoardScreenUIState.Error(BoardScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                issueRepository.getBoardsIssues(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    id = id,
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("BoardScreenViewModel", "Connection Error")
                    _uiState.update {
                        BoardScreenUIState.Error(BoardScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("BoardScreenViewModel", "Error : ${result.error}")
                    when(result.error.code){
                        UNAUTHORIZED -> {
                            authHelper.handleTokenException(
                                onRetry = {getIssues(id,retryCount +1)},
                                onError = { error ->
                                    _uiState.update {
                                        BoardScreenUIState.Error(BoardScreenError(error))
                                    }
                                }
                            )
                        }
                        else -> {
                            _uiState.update {
                                BoardScreenUIState.Error(BoardScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                    _uiState.update {
                        BoardScreenUIState.Error(BoardScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success: ${result.data}")

                    data.issues = result.data

                }
            }
        }
    }

    fun getBoard(id: String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                BoardScreenUIState.Error(BoardScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            _uiState.update {
                BoardScreenUIState.Loading
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
                        BoardScreenUIState.Error(BoardScreenError(R.string.connectionError))
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
                                        BoardScreenUIState.Error(BoardScreenError(error))
                                    }
                                }
                            )
                        }
                        else -> {
                            _uiState.update {
                                BoardScreenUIState.Error(BoardScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                    _uiState.update {
                        BoardScreenUIState.Error(BoardScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success: ${result.data}")

                    data.board = result.data

                    getIssues(id)

                    _uiState.update {
                        BoardScreenUIState.Loaded(data)
                    }
                }
            }

        }
    }
}