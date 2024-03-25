package calculense.platform.process.controller

import calculense.platform.auth.annotation.Paid
import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.getRequestUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("process")
class ProcessController {

    @Autowired
    lateinit var userService: IUserService
    @PostMapping
    @RequiresRole(type = ["user","admin"])
    @Paid
    fun processRequest(@RequestParam("request_id") requestId:String ){
        val user= getRequestUser()
        // process request
        //userService.deductCredit(userId = user.id!!, creditAmount = 5,requestId=requestId)
    }
}