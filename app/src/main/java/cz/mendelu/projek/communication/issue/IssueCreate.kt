package cz.mendelu.projek.communication.issue

data class IssueCreate (
    var boardId: String?,
    var title: String?,
    var description: String?,
    var status: String?,
    var priority: String?,
)