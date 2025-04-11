package edu.ntnu.fullstack.amazoom.listing.mapper

import edu.ntnu.fullstack.amazoom.common.entity.User
import edu.ntnu.fullstack.amazoom.common.mapper.UserMapper
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.category.entity.Category
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequestDto
import edu.ntnu.fullstack.amazoom.listing.dto.ListingDto

/**
 * Mapper class for converting between Listing entities and DTOs.
 */
object ListingMapper {

    /**
     * Converts a CreateOrUpdateListingRequestDto plus a Category and User into a Listing entity.
     * Used when creating a new listing.
     *
     * @param request The request DTO with listing details
     * @param category The category entity for the listing
     * @param seller The user entity who is selling the item
     * @return A new Listing entity
     */
    fun toEntity(request: CreateOrUpdateListingRequestDto, category: Category, seller: User): Listing {
        return Listing(
            title = request.title,
            category = category,
            seller = seller,
            condition = request.condition,
            price = request.price,
            originalPrice = request.originalPrice,
            description = request.description,
            modelYear = request.modelYear,
            manufacturer = request.manufacturer,
            model = request.model,
            serialNumber = request.serialNumber,
            purchaseDate = request.purchaseDate,
            usageDuration = request.usageDuration,
            defects = request.defects ?: emptyList(),
            modifications = request.modifications ?: emptyList(),
            reasonForSelling = request.reasonForSelling,
            images = request.images ?: emptyList(),
            latitude = request.latitude,
            longitude = request.longitude,
        )
    }

    /**
     * Updates an existing Listing entity with values from a CreateOrUpdateListingRequestDto.
     * Used when updating a listing.
     *
     * @param existing The existing Listing entity to update
     * @param request The request DTO with updated values
     * @param category The updated category entity
     * @return The updated Listing entity
     */
    fun updateEntity(
        existing: Listing,
        request: CreateOrUpdateListingRequestDto,
        category: Category
    ): Listing {
        return existing.copy(
            title = request.title,
            category = category,
            condition = request.condition,
            price = request.price,
            originalPrice = request.originalPrice ?: existing.originalPrice,
            description = request.description,
            modelYear = request.modelYear ?: existing.modelYear,
            manufacturer = request.manufacturer ?: existing.manufacturer,
            model = request.model ?: existing.model,
            serialNumber = request.serialNumber ?: existing.serialNumber,
            purchaseDate = request.purchaseDate ?: existing.purchaseDate,
            usageDuration = request.usageDuration ?: existing.usageDuration,
            defects = request.defects ?: existing.defects,
            modifications = request.modifications ?: existing.modifications,
            reasonForSelling = request.reasonForSelling ?: existing.reasonForSelling,
            images = request.images ?: existing.images,
            status = request.status ?: existing.status,
            latitude = request.latitude,
            longitude = request.longitude,
        )
    }

    /**
     * Converts a Listing entity to a ListingDto.
     *
     * @param entity The Listing entity to convert
     * @return The resulting ListingDto
     */
    fun toResponseDto(entity: Listing): ListingDto {
        return ListingDto(
            id = entity.id,
            title = entity.title,
            categoryId = entity.category.id,
            condition = entity.condition,
            price = entity.price,
            originalPrice = entity.originalPrice,
            description = entity.description,
            modelYear = entity.modelYear,
            manufacturer = entity.manufacturer,
            model = entity.model,
            serialNumber = entity.serialNumber,
            purchaseDate = entity.purchaseDate,
            usageDuration = entity.usageDuration,
            defects = entity.defects,
            modifications = entity.modifications,
            reasonForSelling = entity.reasonForSelling,
            seller = UserMapper.toDto(entity.seller),
            createdAt = entity.createdAt,
            images = entity.images,
            status = entity.status,
            isBookmarked = false,
            latitude = entity.latitude,
            longitude = entity.longitude,
        )
    }
}