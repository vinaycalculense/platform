package calculense.platform.auth.model

import java.time.LocalDateTime

data class UserRequestDTO(
    val requestName:String,
    val latestDate: LocalDateTime,
    val url:String
)
