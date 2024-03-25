package calculense.platform.filemanager.service

import calculense.platform.filemanager.model.FileUploadRequestDTO
import calculense.platform.filemanager.model.FileUploadResponseDTO

interface IFileUploadService {
    fun processUpload(fileUploadRequestDTO: FileUploadRequestDTO): FileUploadResponseDTO
    fun getAppNameByRequestId(requestId:String): String

}