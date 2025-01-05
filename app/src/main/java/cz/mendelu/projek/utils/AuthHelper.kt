package cz.mendelu.projek.utils

import android.util.Log
import androidx.compose.ui.res.stringResource
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.auth.AuthRemoteRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthHelper @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val repository: AuthRemoteRepositoryImpl
) {
    suspend fun handleTokenException(
        onRetry: suspend() -> Unit,
        onError: (Int) -> Unit
    ){
        val refreshToken = dataStoreManager.refreshTokenFlow.firstOrNull()
        if(refreshToken == null){
            onError(R.string.refresh_token_missing)
            return
        }

        val result = withContext(Dispatchers.IO){
            repository.refresh(refreshToken)
        }
        when(result){
            is CommunicationResult.ConnectionError -> {
                Log.d("AuthHelper", "ConnectionError")
                onError(R.string.connectionError)
            }
            is CommunicationResult.Error -> {
                Log.d("AuthHelper", "Error: ${result.error}")
                onError(R.string.error)
            }
            is CommunicationResult.Exception -> {
                Log.d("AuthHelper", "Exception: ${result.exception}")
                onError(R.string.exception)
            }
            is CommunicationResult.Success -> {
                Log.d("AuthHelper", "Success: tokens were refreshed")

                if(result.data.accessToken != null && result.data.refreshToken != null){
                    dataStoreManager.updateTokens(
                        accessToken = result.data.accessToken!!,
                        refreshToken = result.data.refreshToken!!,
                    )
                    onRetry()
                }
            }
        }
    }
}