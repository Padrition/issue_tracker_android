package cz.mendelu.projek.communication.issue

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.communication.IBaseRemoteRepository

interface IIssueRemoteRepository: IBaseRemoteRepository {
    suspend fun getBoardsIssues(token: String, id: String): CommunicationResult<List<Issue>>
    suspend fun createIssue(token: String, create: IssueCreate): CommunicationResult<Any>
    suspend fun getIssue(token: String, id: String): CommunicationResult<Issue>
}