package edu.ntnu.fullstack.amazoom.bookmark.exception

/**
 * Exception thrown when attempting to bookmark one's own listing.
 */
class BookmarkOwnListingException(message: String = "You cannot bookmark your own listing.") : RuntimeException(message)