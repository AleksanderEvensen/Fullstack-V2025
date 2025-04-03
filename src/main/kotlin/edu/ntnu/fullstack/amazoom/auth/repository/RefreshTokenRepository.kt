package edu.ntnu.fullstack.amazoom.auth.repository

import edu.ntnu.fullstack.amazoom.auth.entity.RefreshToken
import edu.ntnu.fullstack.amazoom.auth.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository: JpaRepository<RefreshToken, Long> {
    fun findByToken(token: String): RefreshToken?

    @Modifying
    fun deleteByUser(user: User)
    fun token(token: String): MutableList<RefreshToken>
}