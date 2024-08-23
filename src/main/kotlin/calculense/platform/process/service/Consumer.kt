package calculense.platform.process.service

import calculense.platform.filemanager.service.FileUploadService
import calculense.platform.filemanager.service.IFileUploadService
import calculense.platform.process.bean.MessageClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID


@Service
class Consumer {

    @Autowired
    private lateinit var sqsClient: SqsClient

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var fileUploadService: IFileUploadService

    @Scheduled(fixedDelay = 5000) // It runs every 5 seconds.
    fun consumeMessages() {
        try {
            val queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName("catalogue-output").build()).queueUrl()

            val receiveMessageResult = sqsClient.receiveMessage(
                ReceiveMessageRequest.builder().queueUrl(queueUrl).maxNumberOfMessages(10)
                    .build()
            )

            if (receiveMessageResult.hasMessages()) {
                val messages = receiveMessageResult.messages()
                 messages.forEach { it ->
                     val m = it.body()
                     val correctedJsonString = m.toString().replace("'", "\"")
                     val map = objectMapper.readValue(correctedJsonString,Map::class.java)
                     if (map.contains("request_id") && map.contains("response_url"))
                     {
                         var valid=false
                         try{
                             UUID.fromString(map["request_id"].toString().split("_")[0])
                             valid=true
                         }catch(_:Exception){}
                         if(valid) {
                             val uploads =
                                 fileUploadService.getFileUploadByRequestId(map["request_id"].toString().split("_")[0])
                             uploads.forEach { u ->
                                 if (u.key.contains(map["request_id"].toString())) {
                                     u.processed = 2
                                     u.outputKey = map["response_url"].toString()
                                         .split("/")[3] + "/" + map["response_url"].toString().split("/")[4].trim()
                                     u.outputBucket = map["response_url"].toString().split("/")[2].trim()
                                     u.outputDate = LocalDateTime.now(ZoneId.of("UTC"))
                                     fileUploadService.upsert(u)
                                 }
                             }
                         }
                     }
                    sqsClient.deleteMessage { deleteRequest ->
                         deleteRequest.queueUrl(queueUrl).receiptHandle(it.receiptHandle())
                     }
                 }
            }
        } catch (e: Exception) {
            println("Error while consuming messages")
        }
    }
}