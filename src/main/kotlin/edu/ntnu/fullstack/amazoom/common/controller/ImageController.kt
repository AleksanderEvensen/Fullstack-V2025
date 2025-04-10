package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.common.service.MinioService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Profile
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
@Tag(name = "Images", description = "Operations for image upload and retrieval")
@Profile("!test")
class ImageController(private val minioService: MinioService) {

    @Operation(
        summary = "Get image",
        description = "Serves an image by its filename"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Image retrieved successfully",
            content = [Content(mediaType = "image/*")]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Image not found"
        )
    )
    @GetMapping("/{fileName}")
    fun serveImage(@PathVariable fileName: String): ResponseEntity<ByteArray> {
        val objectData = minioService.getImageData(fileName)
        val contentType = minioService.getImageContentType(fileName)

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
            .body(objectData)
    }

    @Operation(
        summary = "Upload image",
        description = "Uploads a new image and returns the generated filename"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Image uploaded successfully",
            content = [Content(mediaType = "text/plain")]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid file",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
    @PostMapping("/upload")
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<String> {
        val fileName = minioService.uploadImage(file, file.originalFilename ?: "default")
        return ResponseEntity.ok(fileName)
    }
}