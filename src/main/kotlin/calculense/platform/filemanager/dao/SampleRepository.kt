package calculense.platform.filemanager.dao

import calculense.platform.filemanager.model.Sample
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SampleRepository:JpaRepository<Sample,Long> {

    fun findByCategoryAndUserIdIsNullOrUserId(category:String,userId:Long):List<Sample>
}