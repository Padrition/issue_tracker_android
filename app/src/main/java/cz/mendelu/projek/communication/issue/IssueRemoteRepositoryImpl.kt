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

}