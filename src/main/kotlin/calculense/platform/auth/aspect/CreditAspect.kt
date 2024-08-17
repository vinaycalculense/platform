package calculense.platform.auth.aspect

import calculense.platform.app.service.IAppService
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.getRequestUser
import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.service.IFileUploadService
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Aspect
@Component
@Order(2)
class CreditAspect {

    @Autowired
    lateinit var userService: IUserService

    @Autowired
    lateinit var fileUploadService: IFileUploadService

    @Autowired
    lateinit var appService: IAppService

    @Autowired
    lateinit var request: HttpServletRequest
    @Before("@annotation(calculense.platform.auth.annotation.Paid)")
    @Order(2)
    fun checkAmount(joinPoint: JoinPoint) {
        val requestId= request.parameterMap["request_id"]?.get(0)?.toString()
        val appName = fileUploadService.getAppNameByRequestId(requestId!!)
        val creditRequired = appService.getAppByName(appName).credits
        val user = getRequestUser()
        if(user.credit<creditRequired){
            throw CalculenseException(errorMessage = "Credit Limit Exhausted", errorCode = 429)
        }else{
            userService.deductCredit(userId = user.id!!, creditAmount = creditRequired,requestId=requestId)
        }

    }

    @AfterThrowing("@annotation(calculense.platform.auth.annotation.Paid)")
    fun deductAmount(joinPoint: JoinPoint) {
        val requestId= request.parameterMap["request_id"]?.get(0)?.toString()
        val appName = fileUploadService.getAppNameByRequestId(requestId!!)
        val creditRequired = appService.getAppByName(appName).credits
        val user = getRequestUser()
        userService.deductCredit(userId = user.id!!, creditAmount = -creditRequired,requestId=requestId)
    }
}