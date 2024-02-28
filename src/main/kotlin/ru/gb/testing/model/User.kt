package ru.gb.testing.model

import jakarta.persistence.*
import ru.gb.testing.dto.UserResponse

@Entity
@Table(name = "users")
data class User(
    val name: String = "",
    @Column(nullable = false)
    val password: String = "",
    @Column(unique = true, nullable = false)
    val email: String = "",
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
) {
    fun toResponse() = UserResponse(id ?: 1, name, email)
}

