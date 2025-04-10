package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.dto.AddressDto
import edu.ntnu.fullstack.amazoom.common.dto.ErrorResponseDto
import edu.ntnu.fullstack.amazoom.common.dto.UpdateAddressRequestDto
import edu.ntnu.fullstack.amazoom.common.dto.UpdateProfileResponseDto
import edu.ntnu.fullstack.amazoom.common.service.IMinIOService
import edu.ntnu.fullstack.amazoom.common.service.MinioService
import edu.ntnu.fullstack.amazoom.common.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
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
@Tag(name = "User", description = "Operations for managing user profiles")
class UserController(private val userService: UserService, private val minIoService: IMinIOService) {
    private val logger = LoggerFactory.getLogger(UserController::class.java)

    /**
     * Updates the user's profile image.
     *
     * @param file The image file to upload
     * @return Response with the URL of the uploaded image
     */
    @Operation(
        summary = "Update profile image",
        description = "Updates the profile image for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Profile image updated successfully",
            content = [Content(schema = Schema(implementation = UpdateProfileResponseDto::class))]
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
    @Operation(
        summary = "Update address",
        description = "Updates the address information for the currently authenticated user"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "200",
            description = "Address updated successfully",
            content = [Content(schema = Schema(implementation = UpdateProfileResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        ),
        ApiResponse(
            responseCode = "401",
            description = "User not authenticated",
            content = [Content(schema = Schema(implementation = ErrorResponseDto::class))]
        )
    )
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