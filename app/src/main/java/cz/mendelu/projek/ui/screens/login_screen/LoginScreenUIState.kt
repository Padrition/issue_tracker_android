package cz.mendelu.projek.ui.screens.login_screen

import java.io.Serializable

sealed class LoginScreenUIState(): Serializable {
    object Idle: LoginScreenUIState()
    object Loading: LoginScreenUIState()
    class LoginCredentialsChanged(var data: LoginScreenData): LoginScreenUIState()
    class Error(val error: LoginScreenError): LoginScreenUIState()
    object SignedIn: LoginScreenUIState()
}