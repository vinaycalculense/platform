package calculense.platform.filemanager.model

import jakarta.persistence.*

@Entity(name = "cl_file_label")
@Table(indexes = [Index(name = "cl_file_label_name_index", columnList = "name", unique = true)])
data class FileLabel(
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        var id:Long?,
        var name:String,
        val description:String,
        val bucket:String,
        val appName:String
)
