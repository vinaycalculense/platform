package calculense.platform.filemanager.model

import jakarta.persistence.*


@Entity(name = "cl_sample_file")
data class SampleFile(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id:Long?,
    var bucket:String,
    var key:String,
    var url:String?=null,
    val category:String
)
