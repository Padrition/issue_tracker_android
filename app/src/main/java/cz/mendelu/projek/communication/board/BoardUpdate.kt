package cz.mendelu.projek.communication.board

import com.squareup.moshi.Json
import cz.mendelu.projek.communication.IdWrapper

data class BoardUpdate (
    var id: String?,
    var name: String?,
    var description: String?,
    var members: MutableList<String>?,
    var categories: MutableList<Category>?,
)