package calculense.platform.process.service

import calculense.platform.exception.CalculenseException
import calculense.platform.filemanager.model.FileLabel
import calculense.platform.filemanager.service.IFileLabelService
import calculense.platform.filemanager.service.IFileUploadService
import calculense.platform.process.model.CatalogueRequest
import calculense.platform.process.util.MessageSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class CatalogueProcessExecutor:ProcessExecutor {

    @Autowired
    lateinit var fileUploadService: IFileUploadService

    @Autowired
    lateinit var fileLabelService: IFileLabelService

    @Autowired
    lateinit var messageSender: MessageSender

    override fun process(requestId: String, requestName:String) {
        val fileUploads= fileUploadService.getFileUploadByRequestId(requestId)
        val processedUpload = fileUploads.find { it.processed==1 || it.processed==2 }
        if(processedUpload!=null){
            throw CalculenseException("Already Processed or Queued",HttpStatus.BAD_REQUEST.value())
        }
        val fileLabels: Map<Long,FileLabel> = fileLabelService.getFileLabelByIDs(fileUploads.map { it.labelId }.toList()).associateBy { it.id!! }
        val fileUploadMapByLabels = fileUploads.associateBy { fileLabels[it.labelId]!!.name }
        val catalogueRequests:MutableList<CatalogueRequest> =  mutableListOf()
        fileUploadMapByLabels.forEach{
            if (it.key.contains("CLOTH") && it.key.contains("BACK")){
                if(fileUploadMapByLabels.contains("MODEL_BACK")){
                    val modelUpload = fileUploadMapByLabels["MODEL_BACK"]!!
                    val request = CatalogueRequest(
                        clothLocation = "s3://${it.value.bucket}/${it.value.key}",
                        modelLocation = "s3://${modelUpload.bucket}/${modelUpload.key}",
                        clothLabel = fileLabels[it.value.labelId]!!.name,
                        modelLabel = fileLabels[modelUpload.labelId]!!.name,
                        category = fileLabels[it.value.labelId]!!.name.replace("CLOTH_","")
                    )
                    catalogueRequests.add(request)
                }else{
                    throw CalculenseException("MODEL BACK NOT INCLUDED IN IMAGE",HttpStatus.BAD_REQUEST.value())
                }
            }else if (it.key.contains("CLOTH")){
                if(fileUploadMapByLabels.contains("MODEL_FRONT")){
                    val modelUpload = fileUploadMapByLabels["MODEL_FRONT"]!!
                    val request = CatalogueRequest(
                        clothLocation = "s3://${it.value.bucket}/${it.value.key}",
                        modelLocation = "s3://${modelUpload.bucket}/${modelUpload.key}",
                        clothLabel = fileLabels[it.value.labelId]!!.name,
                        modelLabel = fileLabels[modelUpload.labelId]!!.name,
                        category = fileLabels[it.value.labelId]!!.name.replace("CLOTH_","")
                    )
                    catalogueRequests.add(request)
                }else{
                    throw CalculenseException("MODEL FRONT NOT INCLUDED IN IMAGE",HttpStatus.BAD_REQUEST.value())
                }

            }
        }
        catalogueRequests.forEach { messageSender.sendMessage(it) }
        fileUploads.forEach{
            it.processed=1
            it.requestName=requestName
            fileUploadService.upsert(it)
        }
    }
}