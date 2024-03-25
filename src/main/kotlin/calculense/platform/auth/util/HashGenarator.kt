package calculense.platform.auth.util

import org.mindrot.jbcrypt.BCrypt

const val saltValue = "$2a$10\$V8.T3ktK3qLzZzIXQ8XueO" // Example salt value


fun hashPassword(password: String): String {
    return BCrypt.hashpw(password, saltValue)
}