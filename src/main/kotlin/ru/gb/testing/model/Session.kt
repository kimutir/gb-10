package ru.gb.testing.model

import jakarta.persistence.*
import java.util.UUID
import java.util.Date

@Entity
@Table(name = "sessions")
data class Session(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,
    val token: UUID = UUID.randomUUID(),
    val expiresAt: Long = Date().time + 10000,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User? = null
) {
    constructor(user: User): this() {
        this.user = user
    }
}