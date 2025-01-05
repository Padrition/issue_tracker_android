package cz.mendelu.projek.communication.board

import cz.mendelu.projek.constants.AUTHORIZATION
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface BoardAPI {

    @GET("board/boards")
    suspend fun getBoards(
        @Header(AUTHORIZATION) token: String
    ): Response<List<Board>>

}