package calculense.platform.model

class Response<T> (
    val data:T?,
    val message:String,
    val error: Boolean
)