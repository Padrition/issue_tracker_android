package cz.mendelu.projek.ui.screens.add_board_screen

import java.io.Serializable

sealed class AddBoardScreenUIState(): Serializable {
    object Idle: AddBoardScreenUIState()
    class BoardNameChanged(var data: AddBoardScreenData): AddBoardScreenUIState()
    class BoardDescriptionChanged(var data: AddBoardScreenData): AddBoardScreenUIState()
    object Loading: AddBoardScreenUIState()
    class Error(var error: AddBoardScreenError): AddBoardScreenUIState()
    object Created: AddBoardScreenUIState()
}