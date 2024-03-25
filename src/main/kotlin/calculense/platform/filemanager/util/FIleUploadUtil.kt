package calculense.platform.filemanager.util

import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration


fun generatePutUrl(s3PreSigner: S3Presigner,bucket:String,key:String):String{
    val request = PutObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

    val preSignRequest: PutObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest(request)
            .build()
    val preSignedRequest: PresignedPutObjectRequest = s3PreSigner.presignPutObject(preSignRequest)
    return preSignedRequest.url().toExternalForm()
}