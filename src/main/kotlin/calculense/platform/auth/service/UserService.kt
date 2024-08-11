package calculense.platform.auth.service

import calculense.platform.auth.dao.CreditLogsRepository
import calculense.platform.auth.dao.UserRepository
import calculense.platform.auth.model.*
import calculense.platform.auth.util.createToken
import calculense.platform.auth.util.hashPassword
import calculense.platform.exception.CalculenseException
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class UserService:IUserService {
    @Autowired
    lateinit var userRepository:UserRepository

    @Autowired
    lateinit var creditLogsRepository: CreditLogsRepository

    override fun signup(user: UserDTO): UserDTO {
       val existingUser = userRepository.findUserByEmail(user.email.lowercase())
       if(existingUser!=null){
           throw CalculenseException(errorMessage = "Email already Exists", errorCode = 400)
       }
        val newUser = getUserFromDTO(user)
        newUser.credit=50
        userRepository.save(newUser)
        return UserDTO(firstName = newUser.firstName,
                lastName = newUser.lastName,
                password = "",
                email = newUser.email,
                credit = newUser.credit
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
                token = createToken(claims),
                credit = existingUser.credit
        )
    }

    override fun getUserById(id:Long): User{
        return userRepository.findUserById(id)
                ?: throw CalculenseException(errorMessage = "User Doesn't exist", errorCode = 500)
    }

    @Transactional
    override fun deductCredit(userId: Long,creditAmount:Int, requestId:String){
        val user = userRepository.findUserById(userId)
        if (user != null) {
            if(user.credit<creditAmount){
                throw CalculenseException(errorMessage = "Credit Limit Exhausted", errorCode = 429)
            }
            creditLogsRepository.save(
                    CreditLogs(
                            id = null,
                            amountDeducted = creditAmount,
                            requestId = requestId,
                            userId = user.id!!
                    )
            )
            user.credit -= creditAmount
            user.modifiedDate= LocalDateTime.now(ZoneId.of("UTC"))
            userRepository.save(user)
        }

    }

}