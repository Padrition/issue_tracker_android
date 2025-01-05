package cz.mendelu.projek.ui.screens.boards_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BoardScreenViewModel @Inject constructor(
    private val repository: BoardRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
): ViewModel() {
    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getBoards(dataStoreManager.accessTokenFlow.first()!!) //TODO Redesign token creation
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("BoardScreenViewModel", "Connection Error")
                }
                is CommunicationResult.Error -> {
                    Log.d("BoardScreenViewModel", "Error : ${result.error}")
                }
                is CommunicationResult.Exception -> {
                    Log.d("BoardScreenViewModel", "Exception : ${result.exception}")
                }
                is CommunicationResult.Success -> {
                    Log.d("BoardScreenViewModel", "Success : ${result.data}")
                }
            }
        }
    }
}