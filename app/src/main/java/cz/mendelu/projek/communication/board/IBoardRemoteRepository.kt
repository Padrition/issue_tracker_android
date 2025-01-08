package cz.mendelu.projek.communication.board

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.IBaseRemoteRepository

interface IBoardRemoteRepository: IBaseRemoteRepository {
    suspend fun getBoards(token: String): CommunicationResult<List<Board>>
    suspend fun createBoard(token: String, create: BoardCreate): CommunicationResult<Any>
    suspend fun getBoard(token: String, id: String): CommunicationResult<Board>
    suspend fun deleteBoard(token: String, id: String): CommunicationResult<Any>
    suspend fun updateBoard(token: String, update: BoardUpdate): CommunicationResult<Any>
}