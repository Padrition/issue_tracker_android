package cz.mendelu.projek.ui.screens.board_screen

import cz.mendelu.projek.communication.board.Board
import cz.mendelu.projek.communication.board.BoardResponse
import cz.mendelu.projek.communication.issue.Issue

data class BoardScreenData(
    var board: BoardResponse = BoardResponse(
        id = null,
        name = null,
        description = null,
        createdBy = null,
        members = null,
        categories = null,
        issues = null,
        isCreator = null
    ),
    var issues: List<Issue> = mutableListOf()
)
