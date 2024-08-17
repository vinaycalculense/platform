package calculense.platform.auth.service

import calculense.platform.auth.dao.CreditLogsRepository
import calculense.platform.auth.dao.UserRepository
import calculense.platform.auth.model.*
import calculense.platform.auth.util.createToken
import calculense.platform.auth.util.hashPassword
import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.service.IFileUploadService
import calculense.platform.filemanager.util.FileUploadUtil
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class UserService:IUserService {
    @Autowired
    lateinit var userRepository:UserRepository

    @Autowired
    lateinit var creditLogsRepository: CreditLogsRepository

    @Autowired
    lateinit var fileUploadService: IFileUploadService

    @Autowired
    lateinit var fileUploadUtil:FileUploadUtil

    private val newUserCredit =50

    override fun signup(user: UserDTO): UserDTO {
       val existingUser = userRepository.findUserByEmail(user.email.lowercase())
       if(existingUser!=null){
           throw CalculenseException(errorMessage = "Email already Exists", errorCode = 400)
       }
        val newUser = getUserFromDTO(user)
        val savedUser = userRepository.save(newUser)
        deductCredit(savedUser.id!!,-newUserCredit,null, description = "Sign up bonus credited")
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
    override fun deductCredit(userId: Long, creditAmount: Int, requestId: String?, description:String?) {
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
                            userId = user.id!!,
                            description = description
                    )
            )
            user.credit -= creditAmount
            user.modifiedDate= LocalDateTime.now(ZoneId.of("UTC"))
            userRepository.save(user)
        }

    }

    override fun getCreditLogs(userId:Long):List<CreditLogs>{
        return creditLogsRepository.findByUserId(userId)
    }

    override fun getRequestLogs(userId: Long):List<UserRequestDTO>{
        val userRequests= mutableListOf<UserRequestDTO>()
        val uploads = fileUploadService.getFileUploadByUserId(userId).filter { it.processed==2}
        val uploadByRequestName = uploads.groupBy { it.requestName }

        uploadByRequestName.forEach {
            val latestUpload= it.value.maxBy { it1 -> it1.createdDate }
            userRequests.add(
                UserRequestDTO(
                    requestName = it.key!!,
                    latestDate = latestUpload.createdDate,
                    url = fileUploadUtil.generateGetUrl(bucket = latestUpload.outputBucket!!,key=latestUpload.outputKey!!, duration = 300)

                )
            )
        }
        return userRequests
    }

    override fun getRequestImage(userId: Long, requestName:String):List<UserRequestDTO>{
        val userRequests= mutableListOf<UserRequestDTO>()
        val uploads = fileUploadService.getFileUploadByUserIdAndRequestName(userId,requestName).filter { it.processed==2}

        uploads.forEach {
            userRequests.add(
                UserRequestDTO(
                    requestName = it.requestName!!,
                    latestDate = it.createdDate,
                    url = fileUploadUtil.generateGetUrl(bucket = it.outputBucket!!,key=it.outputKey!!, duration = 300)

                )
            )
        }
        return userRequests
    }

}