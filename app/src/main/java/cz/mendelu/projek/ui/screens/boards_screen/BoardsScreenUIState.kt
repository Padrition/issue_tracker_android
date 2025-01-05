package cz.mendelu.projek.ui.screens.boards_screen

import java.io.Serializable

sealed class BoardsScreenUIState(): Serializable {
    object Loading: BoardsScreenUIState()
    class Loaded(var data: BoardsScreenData): BoardsScreenUIState()
    class Error(val error: BoardsScreenError): BoardsScreenUIState()
}