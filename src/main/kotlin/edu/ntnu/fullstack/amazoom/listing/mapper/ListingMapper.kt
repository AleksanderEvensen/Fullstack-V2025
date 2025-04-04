package edu.ntnu.fullstack.amazoom.listing.mapper

import edu.ntnu.fullstack.amazoom.auth.entity.User
import edu.ntnu.fullstack.amazoom.auth.mapper.UserMapper
import edu.ntnu.fullstack.amazoom.listing.dto.CreateOrUpdateListingRequest
import edu.ntnu.fullstack.amazoom.listing.dto.ListingResponse
import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.category.entity.Category

object ListingMapper {

    /**
     * Converts a CreateOrUpdateListingRequest plus a Category into a Listing entity.
     */
    fun toEntity(request: CreateOrUpdateListingRequest, category: Category, seller: User): Listing {
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
            images = request.images ?: emptyList()
        )
    }

    /**
     * Converts an existing Listing entity plus a CreateOrUpdateListingRequest and Category
     * into an updated Listing. Typically used for partial or full updates.
     */
    fun updateEntity(
        existing: Listing,
        request: CreateOrUpdateListingRequest,
        category: Category
    ): Listing {
        return existing.copy(
            title = request.title,
            category = category,
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
            images = request.images ?: emptyList()
        )
    }

    /**
     * Converts a Listing entity to a ListingResponse.
     */
    fun toResponseDto(entity: Listing): ListingResponse {
        return ListingResponse(
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
            images = entity.images
        )
    }
}
