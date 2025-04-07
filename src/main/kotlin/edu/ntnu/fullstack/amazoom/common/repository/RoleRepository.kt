package edu.ntnu.fullstack.amazoom.common.repository

import edu.ntnu.fullstack.amazoom.common.entity.Role
import edu.ntnu.fullstack.amazoom.common.entity.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository: JpaRepository<Role, Long> {
    fun findByName(name: RoleName): Role?
}