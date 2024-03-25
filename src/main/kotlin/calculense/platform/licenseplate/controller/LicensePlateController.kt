package calculense.platform.licenseplate.controller

import calculense.platform.auth.annotation.Paid
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.getRequestUser
import jakarta.websocket.server.PathParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("licenseplate")
class LicensePlateController {

    @Autowired
    lateinit var userService: IUserService
    @PostMapping
    @Paid(creditAmount = 5, app = "license-plate-masking")
    fun processRequest(@RequestParam("request_id") requestId:String ){
        val user= getRequestUser()
        // process request
        userService.deductCredit(userId = user.id!!, creditAmount = 5,requestId=requestId)
    }
}