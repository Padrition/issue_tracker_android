package cz.mendelu.projek.ui.screens.add_issue_screen

import cz.mendelu.projek.R
import cz.mendelu.projek.communication.board.BoardResponse
import cz.mendelu.projek.communication.board.Category
import cz.mendelu.projek.communication.issue.IssueCreate

class AddIssueScreenData {
    var issueCreate: IssueCreate = IssueCreate(
        boardId = null,
        title = null,
        description = null,
        status = null,
        priority = null
    )
    var board: BoardResponse = BoardResponse(
        id = null,
        name = null,
        description = null,
        createdBy = null,
        members = null,
        categories = null,
        issues = null,
        isCreator = null
    )
    var selectedCategory = Category(R.string.select_category.toString(), "#123456")
}