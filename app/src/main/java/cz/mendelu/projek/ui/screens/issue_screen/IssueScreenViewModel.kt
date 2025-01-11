package cz.mendelu.projek.ui.screens.issue_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
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
class IssueScreenViewModel @Inject constructor(
    private val repository: IssueRemoteRepositoryImpl,
    private val dataStoreManager: DataStoreManager,
    private val authHelper: AuthHelper,
): ViewModel() {

    private val _uiState: MutableStateFlow<IssueScreenUIState> =
        MutableStateFlow(value = IssueScreenUIState.Loading)

    val uiState: StateFlow<IssueScreenUIState> get() = _uiState.asStateFlow()

    var data = IssueScreenData()

    fun loadIssue(id:String, retryCount: Int = 0){
        if(retryCount > 3){
            _uiState.update {
                IssueScreenUIState.Error(IssueScreenError(R.string.error_max_retry_reched))
            }

            return
        }

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                repository.getIssue(
                    token = dataStoreManager.accessTokenFlow.first()!!,
                    id
                )
            }
            when(result){
                is CommunicationResult.ConnectionError -> {
                    Log.d("IssueScreenViewModel","ConnectionError")
                    _uiState.update {
                        IssueScreenUIState.Error(IssueScreenError(R.string.connectionError))
                    }
                }
                is CommunicationResult.Error -> {
                    Log.d("IssueScreenViewModel","Error : ${result.error}")
                    when(result.error.code){
                        UNAUTHORIZED->{
                            authHelper.handleTokenException(
                                onRetry = {loadIssue(id, retryCount + 1)},
                                onError = {
                                    _uiState.update {
                                        IssueScreenUIState.Error(IssueScreenError(R.string.connectionError))
                                    }
                                }
                            )
                        }
                        else -> {
                            _uiState.update {
                                IssueScreenUIState.Error(IssueScreenError(R.string.connectionError))
                            }
                        }
                    }
                }
                is CommunicationResult.Exception -> {
                    Log.d("IssueScreenViewModel","Exception: ${result.exception}")
                    _uiState.update {
                        IssueScreenUIState.Error(IssueScreenError(R.string.exception))
                    }
                }
                is CommunicationResult.Success -> {
                    Log.d("IssueScreenViewModel","Success: ${result.data}")

                    data.issue = result.data
                    _uiState.update {
                        IssueScreenUIState.Loaded(data)
                    }
                }
            }
        }
    }
}