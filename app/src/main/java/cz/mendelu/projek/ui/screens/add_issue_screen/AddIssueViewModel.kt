package cz.mendelu.projek.ui.screens.add_issue_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.board.BoardRemoteRepositoryImpl
import cz.mendelu.projek.communication.board.Category
import cz.mendelu.projek.communication.issue.IssueRemoteRepositoryImpl
import cz.mendelu.projek.constants.UNAUTHORIZED
import cz.mendelu.projek.ui.screens.add_board_screen.AddBoardScreenError
import cz.mendelu.projek.ui.screens.add_board_screen.AddBoardScreenUIState
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
class AddIssueViewModel @Inject constructor(
    private val issueRepository: IssueRemoteRepositoryImpl,
    private val boardRepository: BoardRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
    private val authHelper: AuthHelper,
): ViewModel() {

    private val _uiState: MutableStateFlow<AddIssueScreenUIState> =
        MutableStateFlow(value = AddIssueScreenUIState.Loading)

    val uiState: StateFlow<AddIssueScreenUIState> get() = _uiState.asStateFlow()

    var data = AddIssueScreenData()

    fun addIssue(id:String, retryCount: Int = 0){
        if(data.issueCreate.title == null || data.issueCreate.description == null){
            _uiState.update {
                AddIssueScreenUIState.cannotBeNull
            }
            return
        }
        if(retryCount > 3){
            _uiState.update {
                AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                issueRepository.createIssue(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    create = data.issueCreate
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("AddIssueViewModel", "Connection Error")
                    _uiState.update {
                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("AddIssueViewModel", "Error: ${result.error}")
                    when(result.error.code){
                        UNAUTHORIZED ->{
                            authHelper.handleTokenException(
                                onRetry = {addIssue(id, retryCount + 1)},
                                onError = {
                                    _uiState.update {
                                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error))
                                    }
                                }
                            )
                        }
                        else ->{
                            _uiState.update {
                                AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("AddIssueViewModel", "Exception: ${result.exception}")
                    _uiState.update {
                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("AddIssueViewModel", "Success: ${result.data}")

                    _uiState.update {
                        AddIssueScreenUIState.Added
                    }
                }
            }
        }

    }

    fun onPriorityChange(priority: String?){
        data.issueCreate.priority = priority

        _uiState.update {
            AddIssueScreenUIState.DataChanged(data)
        }
    }

    fun onCategoryChange(category: Category){
        data.selectedCategory = category
        data.issueCreate.status = category.name

        _uiState.update {
            AddIssueScreenUIState.DataChanged(data)
        }
    }

    fun onDescriptionChange(description: String?){
        data.issueCreate.description = description
        _uiState.update {
            AddIssueScreenUIState.DataChanged(data)
        }
    }

    fun onTitleChange(title: String?){
        data.issueCreate.title = title
        _uiState.update {
            AddIssueScreenUIState.DataChanged(data)
        }
    }

    fun loadBoard(id:String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                boardRepository.getBoard(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    id
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("AddIssueViewModel", "Connection Error")
                    _uiState.update {
                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("AddIssueViewModel", "Error: ${result.error}")
                    when(result.error.code){
                        UNAUTHORIZED ->{
                            authHelper.handleTokenException(
                                onRetry = {loadBoard(id, retryCount + 1)},
                                onError = {
                                    _uiState.update {
                                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error))
                                    }
                                }
                            )
                        }
                        else ->{
                            _uiState.update {
                                AddIssueScreenUIState.Error(AddIssueScreenError(R.string.error))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("AddIssueViewModel", "Exception: ${result.exception}")
                    _uiState.update {
                        AddIssueScreenUIState.Error(AddIssueScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("AddIssueViewModel", "Success: ${result.data}")

                    data.board = result.data
                    result.data.id.let {
                        if(it != null){
                            data.issueCreate.boardId = it
                        }
                    }
                    result.data.categories.let {
                        if(it != null){
                            data.selectedCategory = it.first()
                        }
                    }
                    _uiState.update {
                        AddIssueScreenUIState.Loaded(data)
                    }
                }
            }
        }

    }

}