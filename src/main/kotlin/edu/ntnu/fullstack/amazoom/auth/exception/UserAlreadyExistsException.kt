package edu.ntnu.fullstack.amazoom.auth.exception

class UserAlreadyExistsException(message: String = "User already exists") : RuntimeException(message)