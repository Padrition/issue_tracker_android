package cz.mendelu.projek.ui.screens.board_settings

import cz.mendelu.projek.communication.CommunicationError
import java.io.Serializable

data class BoardSettingScreenError(val communicationError: Int): Serializable