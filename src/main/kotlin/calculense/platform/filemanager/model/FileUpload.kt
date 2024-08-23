package calculense.platform.filemanager.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID
import javax.print.attribute.standard.JobKOctetsProcessed

@Entity(name = "cl_file_upload")
@Table(indexes = [Index(name = "cl_file_upload_request_id_index", columnList = "requestId"),
        Index(name = "cl_file_upload_user_id_index", columnList = "userId"),
        Index(name = "cl_file_upload_created_date_index", columnList = "createdDate")
])
data class FileUpload(
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        val id:UUID? = null,
        val labelId:Long,
        val requestId:UUID,
        val bucket:String,
        val key:String,
        val userId:Long,
        @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
        val createdDate: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC")),
        var processed: Int=0,
        var requestName: String?=null,
        var outputBucket:String?=null,
        var outputKey:String?=null,
        var outputDate: LocalDateTime? = null,
        )
