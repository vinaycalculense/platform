package calculense.platform.filemanager.dao

import calculense.platform.filemanager.model.FileUpload
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FileUploadRepository:JpaRepository<FileUpload,UUID> {

}