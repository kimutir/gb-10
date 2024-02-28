package ru.gb.testing.service

import org.springframework.stereotype.Service
import ru.gb.testing.dto.LoginRequest
import ru.gb.testing.dto.UserRequest
import ru.gb.testing.dto.UserResponse
import ru.gb.testing.exception.UserAuthException
import ru.gb.testing.exception.UserNotFoundException
import ru.gb.testing.model.Session
import ru.gb.testing.model.User
import ru.gb.testing.repository.SessionRepository
import ru.gb.testing.repository.UserRepository
import java.lang.Exception

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {

    fun login(request: LoginRequest, session: Session? = null): UserResponse {
        val user = userRepository.findByNameAndPassword(request.name, request.password)
        user ?: throw UserNotFoundException("You are not registered. Sign up, please.")

        try {
            sessionRepository.save(session ?: Session(user))
        } catch (e: Exception) {
            throw UserAuthException("Authentication problem. Try again later.")
        }

        return user.toResponse()
    }

    fun register(request: UserRequest): UserResponse {
        val user = User(request.name, request.password, request.email)

        try {
            userRepository.save(user)
        } catch (e: Exception) {
            throw UserAuthException("Register problem. Try again later.")
        }

        return user.toResponse()
    }

    fun logout(userId: Int) {
        sessionRepository.deleteByUserId(userId)
    }

}