package cz.mendelu.projek.ui.screens.boards_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.constants.BAD_GATEWAY
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
class BoardsScreenViewModel @Inject constructor(
    private val repository: BoardRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {

    private val _uiState: MutableStateFlow<BoardsScreenUIState> =
        MutableStateFlow(value = BoardsScreenUIState.Loading)
    val uiState: StateFlow<BoardsScreenUIState> get() = _uiState.asStateFlow()

    var data = BoardsScreenData()

    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getBoards(dataStoreManager.accessTokenFlow.first()!!)
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("BoardScreenViewModel", "Connection Error")
                    _uiState.update {
                        BoardsScreenUIState.Error(BoardsScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("BoardScreenViewModel", "Error : ${result.error}")
                    when(result.error.code){
                        BAD_GATEWAY -> {
                            _uiState.update {
                                BoardsScreenUIState.Error(BoardsScreenError(R.string.bad_gateway_error))
                            }
                        }
                        else -> {
                            _uiState.update {
                                BoardsScreenUIState.Error(BoardsScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                    _uiState.update {
                        BoardsScreenUIState.Error(BoardsScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success : ${result.data}")

                    data.boards = result.data

                    _uiState.update {
                        BoardsScreenUIState.Loaded(data)
                    }
                }
            }
        }
    }
}