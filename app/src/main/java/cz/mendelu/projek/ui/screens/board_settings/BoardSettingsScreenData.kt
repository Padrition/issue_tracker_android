package cz.mendelu.projek.ui.screens.board_settings

import cz.mendelu.projek.communication.board.Board
import cz.mendelu.projek.communication.board.BoardResponse

data class BoardSettingsScreenData (
    var board: BoardResponse = BoardResponse(
        id = null,
        name = null,
        description = null,
        isCreator = null,
        members = null,
        categories = null,
        issues = null,
        createdBy = null
    )
)
