package cz.mendelu.projek.ui.screens.board_screen

import java.io.Serializable

sealed class BoardScreenUIState(): Serializable {
    object Idle: BoardScreenUIState()
    object Loading: BoardScreenUIState()
    class Loaded(var data: BoardScreenData): BoardScreenUIState()
    class Error(var error: BoardScreenError): BoardScreenUIState()
}