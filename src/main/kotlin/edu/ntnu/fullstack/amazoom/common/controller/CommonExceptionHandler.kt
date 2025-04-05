package edu.ntnu.fullstack.amazoom.common.controller

import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice(basePackages = ["edu.ntnu.fullstack.amazoom"])
class CommonExceptionHandler {
    // This class can be used to handle common exceptions across the application.
    // You can add methods here to handle specific exceptions and return custom error responses.
    // For example, you can handle NotFoundException, BadRequestException, etc.
    // Each method can return a ResponseEntity with a custom error response.

}