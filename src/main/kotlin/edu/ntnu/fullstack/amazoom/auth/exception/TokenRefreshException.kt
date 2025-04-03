package edu.ntnu.fullstack.amazoom.auth.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN)
class TokenRefreshException(token: String, message: String) :
    RuntimeException("Failed for [$token]: $message")