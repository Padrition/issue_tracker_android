package cz.mendelu.projek.ui.screens.boards_screen

import java.io.Serializable

sealed class BoardScreenUIState(): Serializable {
    object Loading: BoardScreenUIState()
    class Loaded(var data: BoardScreenData): BoardScreenUIState()
}