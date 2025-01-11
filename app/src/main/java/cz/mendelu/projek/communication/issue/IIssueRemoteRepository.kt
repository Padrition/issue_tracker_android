package cz.mendelu.projek.communication.issue

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.IBaseRemoteRepository

interface IIssueRemoteRepository: IBaseRemoteRepository {
    suspend fun getBoardsIssues(token: String, id: String): CommunicationResult<List<Issue>>
}