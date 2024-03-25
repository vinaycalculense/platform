package calculense.platform.filemanager.bean

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Component
class BucketClient {
    private val accessKeyId="AKIA6GBMERFZE5Q2BSRW"
    private val secretAccessKey=""
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