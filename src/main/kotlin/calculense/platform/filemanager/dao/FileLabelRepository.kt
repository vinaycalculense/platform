package calculense.platform.filemanager.dao

import calculense.platform.filemanager.model.FileLabel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FileLabelRepository:JpaRepository<FileLabel,Long> {
    fun findFileLabelByName(name:String):FileLabel?
    fun findFileLabelByNameIn(names:List<String>):List<FileLabel>

    fun findFileLabelById(id:Long):FileLabel
}