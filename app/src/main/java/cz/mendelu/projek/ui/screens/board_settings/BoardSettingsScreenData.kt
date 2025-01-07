package cz.mendelu.projek.ui.screens.board_settings

import cz.mendelu.projek.communication.board.Board

data class BoardSettingsScreenData (
    var board: Board = Board(
        id = null,
        name = null,
        description = null,
        createdBy = null,
        members = null,
        categories = null,
        issues = null
    )
)
