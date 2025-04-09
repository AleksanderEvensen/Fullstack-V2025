package edu.ntnu.fullstack.amazoom.common.mapper

import edu.ntnu.fullstack.amazoom.common.dto.AddressDto
import edu.ntnu.fullstack.amazoom.common.entity.Address

object AddressMapper {
    fun toDto(address: Address): AddressDto {
        return AddressDto(
            streetName = address.streetName,
            streetNumber = address.streetNumber,
            postalCode = address.postalCode,
            city = address.city,
            country = address.country,
        )
    }
}