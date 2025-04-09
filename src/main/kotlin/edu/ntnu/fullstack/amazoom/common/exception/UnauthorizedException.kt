package edu.ntnu.fullstack.amazoom.common.exception

/**
 * Exception thrown when a user attempts to perform an unauthorized action.
 */
class UnauthorizedException(message: String = "Unauthorized action") : RuntimeException(message)