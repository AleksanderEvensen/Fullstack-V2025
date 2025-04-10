package edu.ntnu.fullstack.amazoom.common.service

import edu.ntnu.fullstack.amazoom.common.config.MinIOProperties
import io.minio.BucketExistsArgs
import io.minio.GetObjectArgs
import io.minio.MakeBucketArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.StatObjectArgs
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
@Profile("!test")
class MinioService(
    private val minIOProperties: MinIOProperties
) {
    private val minioClient: MinioClient = MinioClient.builder()
        .endpoint(minIOProperties.endpoint)
        .credentials(minIOProperties.accessKey, minIOProperties.secretKey)
        .build()

    // Initialize bucket
    init {
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(minIOProperties.bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minIOProperties.bucketName).build())
        }
    }

    // Upload an image with a specific name
    fun uploadImage(file: MultipartFile, fileNameBase: String): String {
        val extension = getFileExtension(file.originalFilename)
        val uuid = UUID.randomUUID().toString()
        val baseName = fileNameBase.substringBeforeLast(".", fileNameBase)
        val fileName = "$baseName-$uuid.$extension"

        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(minIOProperties.bucketName)
                .`object`(fileName)
                .stream(file.inputStream, file.size, -1)
                .contentType(file.contentType)
                .build()
        )

        return fileName
    }

    // Get image data for serving through the proxy
    fun getImageData(fileName: String): ByteArray {
        return minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(minIOProperties.bucketName)
                .`object`(fileName)
                .build()
        ).use { it.readAllBytes() }
    }

    fun getImageContentType(fileName: String): String {
        return try {
            val stat = minioClient.statObject(
                StatObjectArgs.builder()
                    .bucket(minIOProperties.bucketName)
                    .`object`(fileName)
                    .build()
            )
            stat.contentType() ?: "image/jpeg"
        } catch (e: Exception) {
            "image/jpeg" // Default if can't determine
        }
    }

    private fun getFileExtension(fileName: String?): String {
        return fileName?.substringAfterLast('.', "jpg") ?: "jpg"
    }
}