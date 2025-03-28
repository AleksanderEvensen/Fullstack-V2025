package edu.ntnu.fullstack.amazoom.controller

import edu.ntnu.fullstack.amazoom.model.User
import edu.ntnu.fullstack.amazoom.service.IUserService
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
class UserController(private val userService: IUserService) {

    /**
 * Retrieves a list of all users.
 *
 * @return a ResponseEntity containing a list of all users.
 */
@GetMapping
@ApiResponse(responseCode = "200", description = "Successfully retrieved users", content = [Content(array = ArraySchema(schema = Schema(implementation = User::class)))])
fun getUsers(): ResponseEntity<List<User>> = ResponseEntity.ok(userService.getAll())

    /**
     * Retrieves a specific user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return a ResponseEntity containing the user, or a 404 status if not found.
     */
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = [Content(schema = Schema(implementation = User::class))])
    @ApiResponse(responseCode = "404", description = "User not found", content = [Content()])
    fun getUsersById(@PathVariable id: Long): ResponseEntity<User> {
        return userService.getById(id)?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }
}