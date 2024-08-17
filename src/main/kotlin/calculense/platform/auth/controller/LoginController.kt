package calculense.platform.auth.controller

import calculense.platform.auth.model.LoginDTO
import calculense.platform.auth.model.UserDTO
import calculense.platform.auth.service.IUserService
import calculense.platform.model.Response
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("login")
@CrossOrigin(origins = ["*"])
class LoginController {

    @Autowired
    lateinit var userService: IUserService
    @PostMapping
    fun login(@RequestBody @Valid loginDTO: LoginDTO): ResponseEntity<Response<UserDTO?>> {
        val newUser=userService.login(loginDTO)
        return ResponseEntity(Response(data = newUser,message="User Login Successful",error=false),HttpStatus.CREATED)

    }
}