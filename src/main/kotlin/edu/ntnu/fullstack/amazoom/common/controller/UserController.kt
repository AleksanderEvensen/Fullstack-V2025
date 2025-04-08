package edu.ntnu.fullstack.amazoom.common.controller

import edu.ntnu.fullstack.amazoom.common.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

}