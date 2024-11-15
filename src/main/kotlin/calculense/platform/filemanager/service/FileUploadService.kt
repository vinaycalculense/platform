package calculense.platform.filemanager.service

import calculense.platform.auth.util.getRequestUser
import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.dao.FileLabelRepository
import calculense.platform.filemanager.dao.FileUploadRepository
import calculense.platform.filemanager.model.FileUpload
import calculense.platform.filemanager.model.FileUploadRequestDTO
import calculense.platform.filemanager.model.FileUploadResponseDTO
import calculense.platform.filemanager.model.URL
import calculense.platform.filemanager.util.FileUploadUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URI
import java.util.UUID

@Service
class FileUploadService:IFileUploadService {
    @Autowired
    lateinit var fileLabelRepository: FileLabelRepository

    @Autowired
    lateinit var fileUploadUtil: FileUploadUtil

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
            urls.add(URL(label=it.name, url = fileUploadUtil.generatePutUrl(bucket = it.bucket, key = requestId.toString()+"_"+it.id+"."+fileUploadRequestDTO.format, duration = 10)))
            val fileUpload = FileUpload(
                requestId=requestId,
                labelId = it.id!!,
                bucket = it.bucket,
                key = requestId.toString()+"_"+it.id+"."+fileUploadRequestDTO.format,
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

    override fun getFileUploadByUserId(userId: Long): List<FileUpload> {
        return fileUploadRepository.findAllByUserId(userId)
    }

    override fun getFileUploadByUserIdAndRequestName(userId: Long, requestName:String): List<FileUpload> {
        return fileUploadRepository.findAllByUserIdAndRequestName(userId,requestName)
    }

    override fun upsert(fileUpload: FileUpload):FileUpload {
        return fileUploadRepository.save(fileUpload)
    }


    override fun copyImage(getUrl: String, putUrl: String) {
        // Open a connection to the GET URL and download the image
        val imageBytes = downloadImageFromGetUrl(getUrl)

        // Upload the downloaded image bytes to the PUT URL
        uploadImageToPutUrl(putUrl, imageBytes)
    }

    private fun downloadImageFromGetUrl(getUrl: String): ByteArray {
        val url = URI.create(getUrl).toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doInput = true

        connection.inputStream.use { inputStream ->
            return inputStream.readBytes()
        }
    }

    private fun uploadImageToPutUrl(putUrl: String, imageBytes: ByteArray) {
        val url = URI.create(putUrl).toURL()
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "image/jpeg") // Adjust content type as needed

        connection.outputStream.use { outputStream ->
            outputStream.write(imageBytes)
        }

        // Check response code to ensure the upload was successful
        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED) {
            throw RuntimeException("Failed to upload image. Response code: $responseCode")
        }
    }

}