package edu.ntnu.fullstack.amazoom.auth.exception

class MissingRoleException(message: String = "The required role is missing in the database") :
    RuntimeException(message)