package edu.ntnu.fullstack.amazoom.bookmark.exception

/**
 * Exception thrown when attempting to create a bookmark that already exists.
 */
class BookmarkAlreadyExistsException(message: String = "Bookmark already exists.") : RuntimeException(message)