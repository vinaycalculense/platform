package calculense.platform.auth.model

import calculense.platform.auth.util.hashPassword
import jakarta.persistence.*
import org.hibernate.resource.beans.container.spi.FallbackContainedBean
import org.springframework.data.annotation.CreatedDate
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(name = "cl_user")
@Table(indexes = [Index(name = "cl_user_email_index", columnList = "email", unique = true)])
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id:Long?,
        val firstName:String,
        val lastName: String,
        val email: String,
        val password:String,
        @Column(columnDefinition = "BOOLEAN DEFAULT false")
        val admin:Boolean=false,
        @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val createdDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
        @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val modifiedDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
)

fun getUserFromDTO(userDTO: UserDTO):User{
        return User(
                id=null,
                firstName = userDTO.firstName,
                lastName = userDTO.lastName,
                email = userDTO.email.lowercase(),
                password = hashPassword(userDTO.password)
                )
}