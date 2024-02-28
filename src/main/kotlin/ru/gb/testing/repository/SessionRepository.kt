package ru.gb.testing.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.gb.testing.model.Session

@Repository
interface SessionRepository: JpaRepository<Session, Int> {
    fun deleteByUserId(userId: Int)
    fun findByUserId(userId: Int): Session?
}