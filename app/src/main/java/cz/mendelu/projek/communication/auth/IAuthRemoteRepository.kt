package cz.mendelu.projek.communication.auth

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.IBaseRemoteRepository

interface IAuthRemoteRepository: IBaseRemoteRepository {
    suspend fun signIn(signIn: SignIn): CommunicationResult<TokenResponse>
    suspend fun refresh(token: String): CommunicationResult<TokenResponse>
    suspend fun create(create: UserCreate): CommunicationResult<Any>
}