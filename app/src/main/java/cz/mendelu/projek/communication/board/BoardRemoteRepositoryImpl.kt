package cz.mendelu.projek.communication.board

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.constants.BEARER
import javax.inject.Inject

class BoardRemoteRepositoryImpl @Inject constructor(private var repository: BoardAPI) : IBoardRemoteRepository {
    override suspend fun getBoards(token: String): CommunicationResult<List<Board>> {
        return try {
            handleResponse(repository.getBoards(BEARER + token))
        }catch (ex: Exception){
            processException(ex)
        }
    }
}