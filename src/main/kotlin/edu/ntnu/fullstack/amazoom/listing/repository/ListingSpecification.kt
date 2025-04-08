package edu.ntnu.fullstack.amazoom.listing.repository

import edu.ntnu.fullstack.amazoom.listing.entity.Listing
import edu.ntnu.fullstack.amazoom.listing.entity.ListingCondition
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
    
    private fun withModelYear(modelYear: String?): Specification<Listing>? {
        return modelYear?.let {
            Specification { root, _, criteriaBuilder ->
                criteriaBuilder.equal(root.get<String>("modelYear"), it)
            }
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

    // Combine all specifications
    fun buildSpecification(
        title: String? = null,
        description: String? = null,
        q: String? = null,
        categoryId: Long? = null,
        condition: ListingCondition? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        modelYear: String? = null,
        manufacturer: String? = null,
        model: String? = null,
        sellerId: Long? = null,
        defectsCount: Int? = null,
        modificationsCount: Int? = null
    ): Specification<Listing> {
        // If q parameter is provided, use it for title/description search
        // otherwise use individual title and description parameters
        val searchSpec = q?.let { withQuery(it) } ?: 
                          Specification.where(withTitle(title))
                                      .and(withDescription(description))
        
        return Specification.where(searchSpec)
            .and(withCategoryId(categoryId))
            .and(withCondition(condition))
            .and(withPriceBetween(minPrice, maxPrice))
            .and(withModelYear(modelYear))
            .and(withManufacturer(manufacturer))
            .and(withModel(model))
            .and(withSellerId(sellerId))
            .and(withDefectsCount(defectsCount))
            .and(withModificationsCount(modificationsCount))
    }
} 