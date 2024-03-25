package calculense.platform.auth.service

import calculense.platform.auth.dao.UserRepository
import calculense.platform.auth.model.LoginDTO
import calculense.platform.auth.model.User
import calculense.platform.auth.model.UserDTO
import calculense.platform.auth.model.getUserFromDTO
import calculense.platform.auth.util.createToken
import calculense.platform.auth.util.hashPassword
import calculense.platform.exception.CalculenseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService:IUserService {
    @Autowired
    lateinit var userRepository:UserRepository

    override fun signup(user: UserDTO): UserDTO {
       val existingUser = userRepository.findUserByEmail(user.email.lowercase())
       if(existingUser!=null){
           throw CalculenseException(errorMessage = "Email already Exists", errorCode = 400)
       }
        val newUser = getUserFromDTO(user)
        userRepository.save(newUser)
        return UserDTO(firstName = user.firstName,
                lastName = user.lastName,
                password = "",
                email = newUser.email,
        )
    }

    override fun login(loginDTO: LoginDTO): UserDTO {
        val existingUser = userRepository.findUserByEmailAndPassword(loginDTO.email.lowercase(), hashPassword(loginDTO.password))
                ?: throw CalculenseException(errorMessage = "Incorrect email/password.", errorCode = 400)
        val role = if (existingUser.admin){
            "admin"
        }else{
            "user"
        }
        val claims = mutableMapOf(Pair<String,Any>("sub",existingUser.email),Pair("role",role),Pair("id",existingUser.id!!))
        return UserDTO(firstName = existingUser.firstName,
                lastName = existingUser.lastName,
                password = "",
                email = existingUser.email,
                token = createToken(claims)
        )
    }

    override fun getUserById(id:Long): User{
        return userRepository.findUserById(id)
                ?: throw CalculenseException(errorMessage = "User Doesn't exist", errorCode = 500)
    }

}