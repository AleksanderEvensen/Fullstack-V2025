package edu.ntnu.fullstack.amazoom.common.exception

class UserAlreadyExistsException(message: String = "User already exists") : RuntimeException(message)