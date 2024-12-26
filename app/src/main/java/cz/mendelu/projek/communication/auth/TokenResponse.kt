package cz.mendelu.projek.communication.auth

import com.squareup.moshi.Json

data class TokenResponse (
    @Json(name = "access_token")
    var accessToken: String?,
    @Json(name = "refresh_token")
    var refreshToken: String?,
)