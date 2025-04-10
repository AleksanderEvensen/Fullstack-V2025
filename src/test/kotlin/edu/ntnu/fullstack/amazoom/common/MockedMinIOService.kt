package edu.ntnu.fullstack.amazoom.common.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Profile("test")
class MockMinIOService : IMinIOService {

    override fun uploadImage(file: MultipartFile, fileNameBase: String): String {
        // In tests, just return a dummy file name
        return "test-image-${System.currentTimeMillis()}.jpg"
    }

    override fun getImageData(fileName: String): ByteArray {
        // Return empty byte array for tests
        return ByteArray(0)
    }

    override fun getImageContentType(fileName: String): String {
        // Return a default content type for tests
        return "image/jpeg"
    }
}