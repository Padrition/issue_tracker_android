package cz.mendelu.projek.communication.issue

import com.squareup.moshi.Json
import cz.mendelu.projek.communication.IdWrapper

data class Issue(
    @Json(name = "_id")
    var id: IdWrapper?,
    var title: String?,
    var description: String?,
    var status: String?,
    var priority: String?,
)