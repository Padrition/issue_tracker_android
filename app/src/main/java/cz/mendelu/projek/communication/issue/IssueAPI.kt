package cz.mendelu.projek.communication.issue

import cz.mendelu.projek.constants.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface IssueAPI {

    @GET("issue/board/{id}")
    suspend fun getBoardsIssues(
        @Header(AUTHORIZATION) token: String,
        @Path("id") id: String,
    ): Response<List<Issue>>
}