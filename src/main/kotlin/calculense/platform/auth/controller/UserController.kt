package calculense.platform.auth.controller

import calculense.platform.auth.annotation.RequiresRole
import calculense.platform.auth.model.CreditLogs
import calculense.platform.auth.model.User
import calculense.platform.auth.model.UserDTO
import calculense.platform.auth.model.UserRequestDTO
import calculense.platform.auth.service.IUserService
import calculense.platform.auth.util.getRequestUser
import calculense.platform.model.Response
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
@CrossOrigin(origins = ["*"])
class UserController {
    @Autowired
    lateinit var userService: IUserService

    @PostMapping
    fun signup(@RequestBody @Valid user: UserDTO): ResponseEntity<Response<UserDTO?>> {
        val newUser = userService.signup(user)
        return ResponseEntity(
            Response(data = newUser, message = "User Created Successfully", error = false),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/txnlog")
    @RequiresRole(type = ["admin", "user"])
    fun txnLog(): ResponseEntity<Response<List<CreditLogs>>> {
        return ResponseEntity(
            Response(
                data = userService.getCreditLogs(getRequestUser().id!!),
                message = "User Credit Logs Fetched Successfully",
                error = false
            ), HttpStatus.OK
        )
    }

    @GetMapping("/requestlog")
    @RequiresRole(type = ["admin", "user"])
    fun requestLog(): ResponseEntity<Response<List<UserRequestDTO>>> {
        return ResponseEntity(
            Response(
                data = userService.getRequestLogs(getRequestUser().id!!),
                message = "User Credit Logs Fetched Successfully",
                error = false
            ), HttpStatus.OK
        )
    }

    @GetMapping("/requestImages")
    @RequiresRole(type = ["admin", "user"])
    fun requestImages(@RequestParam("requestName") requestName:String): ResponseEntity<Response<List<UserRequestDTO>>> {
        return ResponseEntity(
            Response(
                data = userService.getRequestImage(getRequestUser().id!!, requestName),
                message = "User Credit Logs Fetched Successfully",
                error = false
            ), HttpStatus.OK
        )
    }

}
