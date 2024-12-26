package cz.mendelu.projek.communication.auth

import cz.mendelu.projek.communication.CommunicationResult
import javax.inject.Inject

class AuthRemoteRepositoryImpl @Inject constructor(private val repository: AuthAPI): IAuthRemoteRepository {
    override suspend fun signIn(signIn: SignIn): CommunicationResult<TokenResponse> {
        return try{
            handleResponse(repository.signIn(signIn))
        }catch (ex: Exception){
            processException(ex)
        }
    }
}