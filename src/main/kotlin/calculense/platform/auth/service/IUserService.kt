package calculense.platform.auth.service

import calculense.platform.auth.model.LoginDTO
import calculense.platform.auth.model.User
import calculense.platform.auth.model.UserDTO

interface IUserService {
    fun signup(user:UserDTO):UserDTO
    fun login(loginDTO: LoginDTO):UserDTO
    fun getUserById(id: Long): User
}