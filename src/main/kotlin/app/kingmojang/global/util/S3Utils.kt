package app.kingmojang.global.util

import io.awspring.cloud.s3.ObjectMetadata
import io.awspring.cloud.s3.S3Operations
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class S3Utils(
    @Value("\${spring.cloud.aws.s3.bucket}") private val bucket: String,
    private val s3Operations: S3Operations
) {

    fun uploadFile(fileName: String, file: MultipartFile): String {
        file.inputStream.use { inputStream ->
            val resource = s3Operations.upload(
                bucket,
                fileName,
                inputStream,
                ObjectMetadata.builder().contentType(file.contentType).build()
            )
            return resource.url.toString()
        }
    }

    fun deleteFile(s3Url: String) {
        if (s3Url.isNotBlank()) {
            s3Operations.deleteObject(s3Url)
        }
    }
}