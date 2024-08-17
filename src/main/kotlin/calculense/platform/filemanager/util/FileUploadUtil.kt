package calculense.platform.filemanager.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration

@Service
class FileUploadUtil {

    @Autowired
    private lateinit var s3PreSigner: S3Presigner

    fun generatePutUrl(bucket: String, key: String, duration: Long): String {
        val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        val preSignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(duration))
            .putObjectRequest(request)
            .build()
        val preSignedRequest = s3PreSigner.presignPutObject(preSignRequest)
        return preSignedRequest.url().toExternalForm()
    }

    fun generateGetUrl(bucket: String, key: String, duration: Long): String {
        val request = GetObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        val preSignRequest = GetObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(duration))
            .getObjectRequest(request)
            .build()
        val preSignedRequest = s3PreSigner.presignGetObject(preSignRequest)
        return preSignedRequest.url().toExternalForm()
    }

}