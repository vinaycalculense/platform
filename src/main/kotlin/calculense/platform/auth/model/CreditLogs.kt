package calculense.platform.auth.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneId

@Entity(name = "cl_credit_logs")
@Table(indexes = [Index(name = "cl_credit_logs_user_id_index", columnList = "userId", unique = false)])
data class CreditLogs(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        val id:Long?,
        val requestId:String,
        val userId:Long,
        val amountDeducted:Int,
        @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val createdDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
        @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        var modifiedDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
)
