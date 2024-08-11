package calculense.platform.process.util

import calculense.platform.process.bean.MessageClient
import calculense.platform.process.model.CatalogueRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import software.amazon.awssdk.services.sqs.model.SendMessageRequest


@Component
class MessageSender {

    @Autowired
   private lateinit var sqsClient: SqsClient

   @Autowired
   private lateinit var objectMapper:ObjectMapper


   fun sendMessage(request:CatalogueRequest){

        val queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName("catalogue-generation-request").build()).queueUrl()
        sqsClient.sendMessage(SendMessageRequest.builder().queueUrl(queueUrl).messageBody(objectMapper.writeValueAsString(request)).build())

   }

}