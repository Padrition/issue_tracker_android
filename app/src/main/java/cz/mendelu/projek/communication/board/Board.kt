package cz.mendelu.projek.communication.board

import com.squareup.moshi.Json
import cz.mendelu.projek.communication.IdWrapper

data class Board (
    @Json(name = "_id")
    var id: IdWrapper?,
    var name: String?,
    var description: String?,
    var createdBy: String?,
    var members: MutableList<String>?,
    var categories: MutableList<Category>?,
    var issues: List<IdWrapper>?,
)