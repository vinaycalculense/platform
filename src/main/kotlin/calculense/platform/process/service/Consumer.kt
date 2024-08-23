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
                     val map = objectMapper.readValue(m.toString(),Map::class.java)

                     val uploads= fileUploadService.getFileUploadByRequestId(map["request_id"].toString().split("_")[0])
                     uploads.forEach{ u->
                         if(u.key.contains(map["request_id"].toString())){
                             u.processed=2
                             u.outputKey= map["response_url"].toString().split("/")[3] + "/" +map["response_url"].toString().split("/")[4]
                             u.outputBucket= map["response_url"].toString().split("/")[2]
                             fileUploadService.upsert(u)
                         }
                     }
                     sqsClient.deleteMessage { deleteRequest ->
                         deleteRequest.queueUrl(queueUrl).receiptHandle(it.receiptHandle())
                     }
                 }
            }
        } catch (e: Exception) {
            println("Error while consuingoutput messgae")
        }
    }
}