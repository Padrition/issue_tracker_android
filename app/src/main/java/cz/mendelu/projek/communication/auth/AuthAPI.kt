package cz.mendelu.projek.communication.auth

import cz.mendelu.projek.constants.CONTENT_TYPE_APPLICATION_JSON
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthAPI {

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @POST("auth/signin")
    suspend fun signIn(
        @Body signIn: SignIn
    ): Response<TokenResponse>

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @POST("auth/create")
    suspend fun create(
        @Body userCreate: UserCreate
    ): Response<Any>

    @Headers(CONTENT_TYPE_APPLICATION_JSON)
    @POST("auth/refresh")
    suspend fun refresh(
        @Body token: String
    ): Response<TokenResponse>
}