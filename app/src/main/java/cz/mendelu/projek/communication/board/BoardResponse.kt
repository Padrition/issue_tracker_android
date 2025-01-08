package cz.mendelu.projek.communication.board

data class BoardResponse (
    var id: String?,
    var name: String?,
    var description: String?,
    var createdBy: String?,
    var isCreator: Boolean?,
    var members: MutableList<String>?,
    var categories: MutableList<Category>?,
    var issues: List<Any>?,
)