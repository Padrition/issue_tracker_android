package cz.mendelu.projek.communication.issue

import cz.mendelu.projek.communication.CommunicationResult
import cz.mendelu.projek.constants.BEARER
import javax.inject.Inject

class IssueRemoteRepositoryImpl @Inject constructor(private val repository: IssueAPI): IIssueRemoteRepository {
    override suspend fun getBoardsIssues(
        token: String,
        id: String
    ): CommunicationResult<List<Issue>> {
        return try {
            handleResponse(repository.getBoardsIssues(
                token = BEARER + token,
                id = id
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

    override suspend fun createIssue(token: String, create: IssueCreate): CommunicationResult<Any> {
        return try{
            handleResponse(repository.addIssue(
                token = BEARER + token,
                createIssue = create
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

    override suspend fun getIssue(token: String, id: String): CommunicationResult<Issue> {
        return try {
            handleResponse(repository.getIssue(
                token = BEARER + token,
                id = id
            ))
        }catch (ex: Exception){
            processException(ex)
        }
    }

}