package edu.ntnu.fullstack.amazoom.controller

import edu.ntnu.fullstack.amazoom.model.User
import edu.ntnu.fullstack.amazoom.service.IUserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController (private val userService: IUserService) {
    @GetMapping
    fun getUsers(): List<User> = userService.getAll()

    @GetMapping("/{id}")
    fun getUsersById(@PathVariable id: Long): User = userService.getById(id)
}