package calculense.platform.auth.dao

import calculense.platform.auth.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User,Long> {
    fun findUserByEmail(email:String):User?
    fun findUserByEmailAndPassword(email:String, password:String):User?
    fun findUserById(id:Long):User?
}