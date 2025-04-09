package edu.ntnu.fullstack.amazoom.listing.exception

/**
 * Exception thrown when a requested listing cannot be found.
 */
class ListingNotFoundException(message: String = "Listing not found") :
    RuntimeException(message)