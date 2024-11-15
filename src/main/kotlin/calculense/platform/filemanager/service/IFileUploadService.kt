package calculense.platform.filemanager.service

import calculense.platform.filemanager.model.FileUpload
import calculense.platform.filemanager.model.FileUploadRequestDTO
import calculense.platform.filemanager.model.FileUploadResponseDTO

interface IFileUploadService {
    fun processUpload(fileUploadRequestDTO: FileUploadRequestDTO): FileUploadResponseDTO
    fun getAppNameByRequestId(requestId:String): String

    fun getFileUploadByRequestId(requestId: String): List<FileUpload>
    fun upsert(fileUpload: FileUpload): FileUpload
    fun getFileUploadByUserId(userId: Long): List<FileUpload>
    fun getFileUploadByUserIdAndRequestName(userId: Long, requestName: String): List<FileUpload>
    fun copyImage(getUrl: String, putUrl: String)
}