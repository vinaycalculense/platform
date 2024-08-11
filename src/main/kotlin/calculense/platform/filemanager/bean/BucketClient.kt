package calculense.platform.filemanager.bean

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Component
class BucketClient {

    @Value("\${aws.access.key}")
    private lateinit var  accessKeyId: String

    @Value("\${aws.secret.key}")
    private lateinit var secretAccessKey: String

    @Bean
    fun s3PreSignerBean():S3Presigner{
        val credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey)
        return S3Presigner
                .builder()
                .credentialsProvider { credentials }
                .region(Region.AP_SOUTH_1)
                .build()
    }
}