package calculense.platform.filemanager.model

import jakarta.persistence.*

@Entity(name = "cl_sample")
@Table(indexes = [Index(name = "cl_sample_category_index", columnList = "category")])
data class Sample(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id:Long?,
    val category:String,
    val classification:String,
    @OneToMany(cascade = [CascadeType.ALL])
    val sampleFiles: List<SampleFile>,
    val userId: Long?=null
)
