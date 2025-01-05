package cz.mendelu.projek.communication.board

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.IBaseRemoteRepository

interface IBoardRemoteRepository: IBaseRemoteRepository {
    suspend fun getBoards(token: String): CommunicationResult<List<Board>>
    suspend fun createBoard(token: String, create: BoardCreate): CommunicationResult<Any>
}