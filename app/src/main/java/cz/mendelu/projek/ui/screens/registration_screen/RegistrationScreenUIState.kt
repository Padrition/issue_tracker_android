package cz.mendelu.projek.ui.screens.registration_screen

import java.io.Serializable

sealed class RegistrationScreenUIState: Serializable {
    object Idle: RegistrationScreenUIState()
    object Loading: RegistrationScreenUIState()
    class RegistrationCredentialsChanged(var data: RegistrationScreenData): RegistrationScreenUIState()
    class Error(val error: RegistrationScreenError): RegistrationScreenUIState()
    object Registered: RegistrationScreenUIState()
}