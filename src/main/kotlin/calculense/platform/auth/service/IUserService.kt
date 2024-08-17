package calculense.platform.auth.service

import calculense.platform.auth.model.*
import java.util.UUID

interface IUserService {
    fun signup(user:UserDTO):UserDTO
    fun login(loginDTO: LoginDTO):UserDTO
    fun getUserById(id: Long): User
    fun deductCredit(userId: Long, creditAmount: Int, requestId:String?, description:String?)
    fun getCreditLogs(userId: Long): List<CreditLogs>
    fun getRequestLogs(userId: Long): List<UserRequestDTO>
    fun getRequestImage(userId: Long, requestName: String): List<UserRequestDTO>
}