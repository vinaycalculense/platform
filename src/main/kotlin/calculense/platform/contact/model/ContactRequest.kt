package calculense.platform.contact.model

import jakarta.persistence.*

@Entity(name = "cl_contact_request")
@Table(indexes = [Index(name = "cl_contact_request_index", columnList = "email")])
data class ContactRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id:Long?,
    val name:String,
    val email:String,
    val message:String
)
