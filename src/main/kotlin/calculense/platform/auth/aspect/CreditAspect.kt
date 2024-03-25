package calculense.platform.auth.aspect

import calculense.platform.auth.annotation.Paid
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.getRequestUser
import calculense.platform.exception.CalculenseException
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(2)
class CreditAspect {

    @Autowired
    lateinit var userService: IUserService

    @Before("@annotation(calculense.platform.auth.annotation.Paid)")
    fun checkAmount(joinPoint: JoinPoint) {
        val method = (joinPoint.signature as MethodSignature).method
        val annotation = method.getAnnotation(Paid::class.java)
        val requiredCreditAmount = annotation.creditAmount
        val user = getRequestUser()
        if(user.credit<requiredCreditAmount){
            throw CalculenseException(errorMessage = "Credit Limit Exhausted", errorCode = 429)
        }
    }
}