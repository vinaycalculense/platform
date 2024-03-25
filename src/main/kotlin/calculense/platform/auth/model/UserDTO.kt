package calculense.platform.auth.model

import jakarta.validation.constraints.Pattern

data class UserDTO(
        val firstName:String,
        val lastName: String,
        @field:Pattern(
                regexp = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})",
                message = "Invalid email format"
        )
        val email: String,
        @field:Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
                message = "Password must be at least 8 characters long and contain at least one digit, one lowercase letter, and one uppercase letter"
        )
        val password:String,
        val token:String?=null,
        val credit:Int=0
)
