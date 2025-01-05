package cz.mendelu.projek.ui.screens.board_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
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
    private val dataStoreManager: DataStoreManager,
): ViewModel() {

    private val _uiState: MutableStateFlow<BoardScreenUIState> =
        MutableStateFlow(value = BoardScreenUIState.Idle)
    val uiState: StateFlow<BoardScreenUIState> get() = _uiState.asStateFlow()

    var data = BoardScreenData()

    fun getBoard(id: String){
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
                    _uiState.update {
                        BoardScreenUIState.Error(BoardScreenError(R.string.error))
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

                    _uiState.update {
                        BoardScreenUIState.Loaded(data)
                    }
                }
            }

        }
    }
}