package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.dto.AddressDto
import edu.ntnu.fullstack.amazoom.common.dto.UpdateAddressRequestDto
import edu.ntnu.fullstack.amazoom.common.dto.UpdateProfileResponseDto
import edu.ntnu.fullstack.amazoom.common.service.MinioService
import edu.ntnu.fullstack.amazoom.common.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * REST Controller for managing user-related operations.
 */
@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService, private val minIoService: MinioService) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    /**
     * Updates the user's profile image.
     *
     * @param file The image file to upload
     * @return Response with the URL of the uploaded image
     */
    @PostMapping("/profile-image", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateProfileImage(@RequestParam("file") file: MultipartFile): ResponseEntity<UpdateProfileResponseDto> {
        logger.info("Updating profile image for user")

        // Upload the image to MinIO and get the filename
        val fileName = minIoService.uploadImage(file, file.originalFilename ?: "default")

        // Update the user's profile with the image URL
        userService.updateProfileImage(fileName)

        return ResponseEntity.ok(UpdateProfileResponseDto("Profile image updated successfully"))
    }

    /**
     * Updates the user's address information.
     *
     * @param request The address update request
     * @return Response indicating successful update
     */
    @PutMapping("/address")
    fun updateAddress(@Valid @RequestBody request: UpdateAddressRequestDto): ResponseEntity<UpdateProfileResponseDto> {
        logger.info("Updating address for user")

        userService.updateAddress(
            AddressDto(
                streetName = request.streetName,
                streetNumber = request.streetNumber,
                postalCode = request.postalCode,
                city = request.city,
                country = request.country
            )
        )

        return ResponseEntity.ok(UpdateProfileResponseDto("Address updated successfully"))
    }

}