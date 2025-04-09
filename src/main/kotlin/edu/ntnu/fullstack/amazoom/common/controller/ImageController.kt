package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.service.MinioService
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/api/images")
class ImageController(private val minioService: MinioService) {

    @GetMapping("/{fileName}")
    fun serveImage(@PathVariable fileName: String): ResponseEntity<ByteArray> {
        val objectData = minioService.getImageData(fileName)
        val contentType = minioService.getImageContentType(fileName)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
            .body(objectData)
    }

    @PostMapping("/upload")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val fileName = minioService.uploadImage(file, file.originalFilename ?: "default")
        return ResponseEntity.ok(fileName)
    }
}