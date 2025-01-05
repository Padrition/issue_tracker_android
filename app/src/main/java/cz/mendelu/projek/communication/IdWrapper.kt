package cz.mendelu.projek.communication

import com.squareup.moshi.Json

data class IdWrapper (
    @Json(name = "\$oid")
    var oid: String?
)