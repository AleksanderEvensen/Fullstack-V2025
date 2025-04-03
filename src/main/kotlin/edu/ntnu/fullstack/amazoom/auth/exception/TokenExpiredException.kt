package edu.ntnu.fullstack.amazoom.auth.exception

class TokenExpiredException(message: String = "Token has expired") : RuntimeException(message)