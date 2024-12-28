package cz.mendelu.projek.ui.screens.registration_screen

import cz.mendelu.projek.communication.auth.UserCreate

data class RegisterScreenData(
    var create: UserCreate = UserCreate(null, null, null),
    var passwordCheck: String? = null
)
