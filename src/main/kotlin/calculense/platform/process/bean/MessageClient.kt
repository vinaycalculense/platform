package calculense.platform.process.bean

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient

@Component
class MessageClient {

    @Value("\${aws.access.key}")
    private lateinit var  accessKeyId: String

    @Value("\${aws.secret.key}")
    private lateinit var secretAccessKey: String

    @Bean
    fun amazonSQSClient(): SqsClient {
        val awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)
        return SqsClient.builder().credentialsProvider {awsCredentials }
            .region(Region.AP_SOUTH_1)
            .build()
    }



}