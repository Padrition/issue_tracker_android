package cz.mendelu.projek.ui.screens.login_screen

import cz.mendelu.projek.communication.CommunicationError
import java.io.Serializable

data class LoginScreenError(val communicationError: Int): Serializable