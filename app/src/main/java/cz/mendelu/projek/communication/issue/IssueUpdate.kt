package cz.mendelu.projek.communication.issue

data class IssueUpdate (
    var id: String?,
    var title: String?,
    var description: String?,
    var status: String?,
    var priority: String?,
)