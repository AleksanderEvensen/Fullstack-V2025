package edu.ntnu.fullstack.amazoom.auth.repository

import edu.ntnu.fullstack.amazoom.auth.entity.Role
import edu.ntnu.fullstack.amazoom.auth.entity.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: RoleName): Role?
}