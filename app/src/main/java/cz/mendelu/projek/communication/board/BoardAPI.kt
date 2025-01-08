package cz.mendelu.projek.communication.board

import cz.mendelu.projek.constants.AUTHORIZATION
import cz.mendelu.projek.constants.CONTENT_TYPE_APPLICATION_JSON
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface BoardAPI {

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @GET("board/boards")
    suspend fun getBoards(
        @Header(AUTHORIZATION) token: String
    ): Response<List<Board>>

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @POST("board/create")
    suspend fun createBoard(
        @Header(AUTHORIZATION) token: String,
        @Body boardCreate: BoardCreate
    ): Response<Any>

    @GET("board/get/{id}")
    suspend fun getBoard(
        @Header(AUTHORIZATION) token: String,
        @Path("id") id: String
    ): Response<Board>

    @DELETE("board/delete/{id}")
    suspend fun deleteBoard(
        @Header(AUTHORIZATION) token: String,
        @Path("id") id: String
    ): Response<Any>

    @PUT("board/update")
    suspend fun updateBoard(
        @Header(AUTHORIZATION) token: String,
        @Body boardUpdate: BoardUpdate
    ): Response<Any>

}