package cz.mendelu.projek.communication.issue

import cz.mendelu.projek.constants.AUTHORIZATION
import cz.mendelu.projek.constants.CONTENT_TYPE_APPLICATION_JSON
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface IssueAPI {

    @GET("issue/board/{id}")
    suspend fun getBoardsIssues(
        @Header(AUTHORIZATION) token: String,
        @Path("id") id: String,
    ): Response<List<Issue>>

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @POST("issue/create")
    suspend fun addIssue(
        @Header(AUTHORIZATION) token: String,
        @Body createIssue: IssueCreate,
    ): Response<Any>

    @GET("issue/{id}")
    suspend fun getIssue(
        @Header(AUTHORIZATION) token: String,
        @Path("id") id: String,
    ): Response<Issue>
}