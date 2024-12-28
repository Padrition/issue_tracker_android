package cz.mendelu.projek.communication.auth

data class UserCreate (
    var email: String?,
    var login: String?,
    var password: String?,
)