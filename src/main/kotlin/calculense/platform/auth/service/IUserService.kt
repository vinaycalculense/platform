package calculense.platform.auth.service

import calculense.platform.auth.model.LoginDTO
import calculense.platform.auth.model.User
import calculense.platform.auth.model.UserDTO
import java.util.UUID

interface IUserService {
    fun signup(user:UserDTO):UserDTO
    fun login(loginDTO: LoginDTO):UserDTO
    fun getUserById(id: Long): User
    fun deductCredit(userId: Long, creditAmount: Int, requestId:String)
}