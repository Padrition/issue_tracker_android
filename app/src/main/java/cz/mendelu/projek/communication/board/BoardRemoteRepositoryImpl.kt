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

    override suspend fun createBoard(token: String, create: BoardCreate): CommunicationResult<Any> {
        return try {
            handleResponse(repository.createBoard(
                token = BEARER + token,
                boardCreate = create
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

    override suspend fun getBoard(token: String, id: String): CommunicationResult<BoardResponse> {
        return try {
            handleResponse(repository.getBoard(
                token = BEARER + token,
                id = id,
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

    override suspend fun deleteBoard(token: String, id: String): CommunicationResult<Any> {
        return try{
            handleResponse(repository.deleteBoard(
                token = BEARER + token,
                id = id
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

    override suspend fun updateBoard(token: String, update: BoardUpdate): CommunicationResult<Any> {
        return try {
            handleResponse(repository.updateBoard(
                token = BEARER + token,
                boardUpdate = update
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }
}