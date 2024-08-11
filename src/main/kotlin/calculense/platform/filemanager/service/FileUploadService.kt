package calculense.platform.filemanager.service

import calculense.platform.auth.util.getRequestUser
import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.dao.FileLabelRepository
import calculense.platform.filemanager.dao.FileUploadRepository
import calculense.platform.filemanager.model.FileUpload
import calculense.platform.filemanager.model.FileUploadRequestDTO
import calculense.platform.filemanager.model.FileUploadResponseDTO
import calculense.platform.filemanager.model.URL
import calculense.platform.filemanager.util.generatePutUrl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import java.util.UUID

@Service
class FileUploadService:IFileUploadService {
    @Autowired
    lateinit var fileLabelRepository: FileLabelRepository

    @Autowired
    lateinit var s3PreSigner: S3Presigner

    @Autowired
    lateinit var fileUploadRepository: FileUploadRepository
    override fun processUpload(fileUploadRequestDTO: FileUploadRequestDTO): FileUploadResponseDTO {
       val labels = fileUploadRequestDTO.labels.map { it.uppercase() }.toList()
        val fileLabels= fileLabelRepository.findFileLabelByNameIn(labels)
        if(fileLabels.size!=labels.size){
            throw CalculenseException(errorMessage = "File Label Doesn't exists", errorCode = 400)
        }
        val requestId= UUID.randomUUID()
        val urls= mutableListOf<URL>()
        val user= getRequestUser()
        fileLabels.forEach {
            urls.add(URL(label=it.name, url = generatePutUrl(s3PreSigner = s3PreSigner, bucket = it.bucket, key = requestId.toString()+"-"+it.id+"."+fileUploadRequestDTO.format)))
            val fileUpload = FileUpload(
                requestId=requestId,
                labelId = it.id!!,
                bucket = it.bucket,
                key = requestId.toString()+"-"+it.id+"."+fileUploadRequestDTO.format,
                userId = user.id!!
            )
           fileUploadRepository.save(fileUpload)
        }
        return FileUploadResponseDTO(requestId=requestId,urls=urls)
    }

    override fun getAppNameByRequestId(requestId: String): String {
        val labelId= fileUploadRepository.findFirstByRequestId(UUID.fromString(requestId)).labelId
        return fileLabelRepository.findFileLabelById(labelId).appName
    }

    override fun getFileUploadByRequestId(requestId: String): List<FileUpload> {
       return fileUploadRepository.findAllByRequestId(UUID.fromString(requestId))
    }

    override fun upsert(fileUpload: FileUpload):FileUpload {
        return fileUploadRepository.save(fileUpload)
    }

}