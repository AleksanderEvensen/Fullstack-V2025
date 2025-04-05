package edu.ntnu.fullstack.amazoom.listing.exception

class ListingNotFoundException(message: String = "Listing not found") :
    RuntimeException(message)