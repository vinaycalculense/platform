package calculense.platform.app.model

import jakarta.persistence.*

@Entity(name = "cl_app")
@Table(indexes = [Index(name = "cl_app_name_index", columnList = "name", unique = true)])

data class App(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id:Long?,
        val name:String,
        val credits:Int
)
