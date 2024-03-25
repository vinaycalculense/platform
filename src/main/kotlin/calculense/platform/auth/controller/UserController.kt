package calculense.platform.auth.controller

import calculense.platform.auth.model.User
import calculense.platform.auth.model.UserDTO
import calculense.platform.auth.service.IUserService
import calculense.platform.model.Response
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController {
    @Autowired
    lateinit var userService: IUserService
    @PostMapping
     fun signup(@RequestBody @Valid user: UserDTO): ResponseEntity<Response<UserDTO?>> {
        val newUser=userService.signup(user)
        return ResponseEntity(Response(data = newUser,message="User Created Successfully",error=false),HttpStatus.CREATED)
    }
}