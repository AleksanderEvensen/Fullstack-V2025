package edu.ntnu.fullstack.amazoom.listing.repository

import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
import edu.ntnu.fullstack.amazoom.listing.entity.ListingStatus
import jakarta.persistence.criteria.JoinType
import org.springframework.data.jpa.domain.Specification

object ListingSpecification {
    
    private fun withTitle(title: String?): Specification<Listing>? {
        return title?.let { 
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%${it.lowercase()}%"
                )
            }
        }
    }

    private fun withStatus(status: ListingStatus): Specification<Listing> {
        return status.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<ListingStatus>("status"), it)
            }
        }
    }
    
    private fun withDescription(description: String?): Specification<Listing>? {
        return description?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%${it.lowercase()}%"
                )
            }
        }
    }
    
    /**
     * Search in both title and description fields with a single query parameter
     */
    private fun withQuery(query: String?): Specification<Listing>? {
        return query?.let {
            Specification { root, _, criteriaBuilder ->
                val titlePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%${it.lowercase()}%"
                )
                val descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    "%${it.lowercase()}%"
                )
                criteriaBuilder.or(titlePredicate, descriptionPredicate)
            }
        }
    }
    
    private fun withCategoryId(categoryId: Long?): Specification<Listing>? {
        return categoryId?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Any>("category").get<Long>("id"), it)
            }
        }
    }
    
    private fun withCategoryName(categoryName: String?): Specification<Listing>? {
        return categoryName?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Any>("category").get<String>("name"), it)
            }
        }
    }
    
    private fun withCondition(condition: ListingCondition?): Specification<Listing>? {
        return condition?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<ListingCondition>("condition"), it)
            }
        }
    }
    
    private fun withPriceBetween(minPrice: Double?, maxPrice: Double?): Specification<Listing>? {
        return when {
            minPrice != null && maxPrice != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.between(root.get("price"), minPrice, maxPrice)
                }
            }
            minPrice != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)
                }
            }
            maxPrice != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)
                }
            }
            else -> null
        }
    }

    /**
     * Filter by model year
     */
    private fun withModelYearBetween(
        minModelYear: Int?,
        maxModelYear: Int?
    ): Specification<Listing>? {
        return when {
            minModelYear != null && maxModelYear != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.between(root.get("modelYear"), minModelYear, maxModelYear)
                }
            }
            minModelYear != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("modelYear"), minModelYear)
                }
            }
            maxModelYear != null -> {
                Specification { root, _, criteriaBuilder ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("modelYear"), maxModelYear)
                }
            }
            else -> null
        }
    }
    
    private fun withManufacturer(manufacturer: String?): Specification<Listing>? {
        return manufacturer?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("manufacturer")),
                    "%${it.lowercase()}%"
                )
            }
        }
    }
    
    private fun withModel(model: String?): Specification<Listing>? {
        return model?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("model")),
                    "%${it.lowercase()}%"
                )
            }
        }
    }
    
    private fun withSellerId(sellerId: Long?): Specification<Listing>? {
        return sellerId?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<Any>("seller").get<Long>("id"), it)
            }
        }
    }
    
    private fun withDefectsCount(count: Int?): Specification<Listing>? {
        return count?.let {
            Specification { root, query, criteriaBuilder ->
                val defectsJoin = root.join<Listing, List<String>>("defects", JoinType.LEFT)
                query?.groupBy(root.get<Long>("id"))
                criteriaBuilder.equal(criteriaBuilder.count(defectsJoin), count.toLong())
            }
        }
    }
    
    private fun withModificationsCount(count: Int?): Specification<Listing>? {
        return count?.let {
            Specification { root, query, criteriaBuilder ->
                val modificationsJoin = root.join<Listing, List<String>>("modifications", JoinType.LEFT)
                query?.groupBy(root.get<Long>("id"))
                criteriaBuilder.equal(criteriaBuilder.count(modificationsJoin), count.toLong())
            }
        }
    }

    private fun withinRadius(latitude: Double?, longitude: Double?, radiusKm: Double?): Specification<Listing>? {
        if (latitude == null || longitude == null || radiusKm == null) {
            return null
        }
        return Specification { root, _, criteriaBuilder ->
            val distanceInMeters = criteriaBuilder.function(
                "ST_Distance_Sphere",
                Double::class.java,
                criteriaBuilder.function("POINT", Any::class.java, root.get<Double>("longitude"), root.get<Double>("latitude")),
                criteriaBuilder.function("POINT", Any::class.java, criteriaBuilder.literal(longitude), criteriaBuilder.literal(latitude))
            )
            val radiusInMeters = radiusKm * 1000
            criteriaBuilder.lessThanOrEqualTo(distanceInMeters, radiusInMeters)
        }
    }

    // Combine all specifications
    fun buildSpecification(
        title: String? = null,
        description: String? = null,
        q: String? = null,
        categoryId: Long? = null,
        categoryName: String? = null,
        condition: ListingCondition? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minModelYear: Int? = null,
        maxModelYear: Int? = null,
        manufacturer: String? = null,
        model: String? = null,
        sellerId: Long? = null,
        defectsCount: Int? = null,
        modificationsCount: Int? = null,
        status: ListingStatus = ListingStatus.ACTIVE
        latitude: Double? = null,
        longitude: Double? = null,
        radiusKm: Double? = null
    ): Specification<Listing> {
        // If q parameter is provided, use it for title/description search
        // otherwise use individual title and description parameters
        val searchSpec = q?.let { withQuery(it) } ?: 
                          Specification.where(withTitle(title))
                                      .and(withDescription(description))
        
        return Specification.where(searchSpec)
            .and(withCategoryId(categoryId))
            .and(withCategoryName(categoryName))
            .and(withCondition(condition))
            .and(withPriceBetween(minPrice, maxPrice))
            .and(withModelYearBetween(minModelYear, maxModelYear))
            .and(withManufacturer(manufacturer))
            .and(withModel(model))
            .and(withSellerId(sellerId))
            .and(withDefectsCount(defectsCount))
            .and(withModificationsCount(modificationsCount))
            .and(withStatus(status))
            .and(withinRadius(latitude, longitude, radiusKm))
    }
}