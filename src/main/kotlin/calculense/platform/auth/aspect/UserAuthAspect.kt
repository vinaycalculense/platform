package calculense.platform.auth.aspect

import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.parseToken
import calculense.platform.exception.CalculenseException
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
@Order(1)
class UserAuthAspect {

    @Autowired
    lateinit var request: HttpServletRequest

    @Autowired
    lateinit var userService: IUserService

    @Before("@annotation(calculense.platform.auth.annotation.RequiresRole)")
    fun authorizeRole(joinPoint: JoinPoint) {

        val method = (joinPoint.signature as MethodSignature).method
        val requiresRole = method.getAnnotation(RequiresRole::class.java)

        val requiredRole = requiresRole.type
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw CalculenseException(errorMessage = "UnAuthenticated." , errorCode = 401)
        val claims = parseToken(token)

        if (!requiredRole.contains(claims["role"].toString().lowercase())) {
            throw CalculenseException(errorMessage = "UnAuthorized.", errorCode = 403)
        }
        val requestAttributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
        requestAttributes.setAttribute("user",userService.getUserById(claims["id"].toString().toLong()),1)
    }
}