package edu.ntnu.fullstack.amazoom.auth.exception

/**
 * Exception thrown when a user attempts to authenticate with invalid credentials.
 */
class InvalidCredentialsException(message: String = "Invalid credentials") :
    RuntimeException(message)