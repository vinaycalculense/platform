package calculense.platform.auth.util

import calculense.platform.auth.model.User
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

fun getRequestUser():User{
    val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
    return requestAttributes.getAttribute("user", 1) as User
}