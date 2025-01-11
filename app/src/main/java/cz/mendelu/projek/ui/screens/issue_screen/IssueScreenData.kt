package cz.mendelu.projek.ui.screens.issue_screen

import cz.mendelu.projek.communication.issue.Issue

class IssueScreenData {
    var issue: Issue = Issue(
        id = null,
        title = null,
        description = null,
        status = null,
        priority = null
    )
}