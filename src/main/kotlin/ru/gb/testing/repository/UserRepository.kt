package ru.gb.testing.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.gb.testing.model.User

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByNameAndPassword(name: String, password: String): User?
    fun findByEmail(email: String): User?
}