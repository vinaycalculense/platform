package calculense.platform.process.model

import software.amazon.awssdk.services.s3.endpoints.internal.Value.Str

data class CatalogueRequest(
    val clothLocation:String,
    val modelLocation: String,
    val clothLabel:String,
    val modelLabel:String,
    val category:String
)
