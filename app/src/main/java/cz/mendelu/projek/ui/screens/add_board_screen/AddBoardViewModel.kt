package cz.mendelu.projek.ui.screens.add_board_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
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
class AddBoardViewModel @Inject constructor(
    private val repository: BoardRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
    private val authHelper: AuthHelper,
): ViewModel() {
    private val _uiState: MutableStateFlow<AddBoardScreenUIState> =
        MutableStateFlow(value = AddBoardScreenUIState.Idle)

    val uiState: StateFlow<AddBoardScreenUIState> get() = _uiState.asStateFlow()

    var data = AddBoardScreenData()

    fun onNameChange(name: String?){
        data.boardCreate.name = name

        _uiState.update {
            AddBoardScreenUIState.BoardNameChanged(data)
        }
    }

    fun onDescriptionChange(description: String?){
        data.boardCreate.description = description

        _uiState.update {
            AddBoardScreenUIState.BoardDescriptionChanged(data)
        }
    }

    fun createBoard(retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                AddBoardScreenUIState.Error(AddBoardScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            _uiState.update {
                AddBoardScreenUIState.Loading
            }

            val result = withContext(Dispatchers.IO){
                repository.createBoard(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    create = data.boardCreate
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("AddBoardViewModel", "ConnectionError")
                    _uiState.update {
                        AddBoardScreenUIState.Error(AddBoardScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("AddBoardViewModel", "Error : ${result.error}")

                    when(result.error.code){
                        UNAUTHORIZED -> {
                            authHelper.handleTokenException(
                                onRetry = {createBoard(retryCount +1)},
                                onError = { error ->
                                    _uiState.update {
                                        AddBoardScreenUIState.Error(AddBoardScreenError(error))
                                    }
                                }
                            )
                        }
                        else ->{
                            _uiState.update {
                                AddBoardScreenUIState.Error(AddBoardScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("AddBoardViewModel", "Exception: ${result.exception}")
                    _uiState.update {
                        AddBoardScreenUIState.Error(AddBoardScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("AddBoardViewModel", "Success: ${result.data}")

                    _uiState.update {
                        AddBoardScreenUIState.Created
                    }
                }
            }
        }
    }
}