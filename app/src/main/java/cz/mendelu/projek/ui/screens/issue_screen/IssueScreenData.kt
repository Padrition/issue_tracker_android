package cz.mendelu.projek.ui.screens.issue_screen

import cz.mendelu.projek.R
import cz.mendelu.projek.communication.board.BoardResponse
import cz.mendelu.projek.communication.board.Category
import cz.mendelu.projek.communication.issue.Issue

class IssueScreenData {
    var issue: Issue = Issue(
        id = null,
        title = null,
        description = null,
        status = null,
        priority = null
    )
    var issueEdit: Issue = Issue(
        id = null,
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
        isCreator = null,
        members = null,
        categories = null,
        issues = null
    )
    var selectedCategory = Category(R.string.select_category.toString(), "#123456")
}