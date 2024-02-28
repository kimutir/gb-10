package ru.gb.testing.service

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.gb.testing.dto.LoginRequest
import ru.gb.testing.dto.UserRequest
import ru.gb.testing.exception.UserNotFoundException
import ru.gb.testing.model.Session
import ru.gb.testing.model.User
import ru.gb.testing.repository.SessionRepository
import ru.gb.testing.repository.UserRepository

class AuthServiceTest {
    private val userRepository: UserRepository = mockk()
    private val sessionRepository: SessionRepository = mockk()
    private val authService: AuthService = AuthService(userRepository, sessionRepository)

    private val user = User("Timur", "pass", "e@mail.ru")
    private val session = Session(user)

    @Test
    fun `saves user session on login`() {
        every { userRepository.findByNameAndPassword(user.name, user.password) } returns user
        every { sessionRepository.save(session) } returns session

        val loggedUser = authService.login(LoginRequest(user.name, user.password), session)

        assertThat(loggedUser == user.toResponse())
    }

    @Test
    fun `throws UserNotFound exception with wrong credentials`() {
        every { userRepository.findByNameAndPassword(user.name, user.password) } returns null

        assertThrows<UserNotFoundException> { authService.login(LoginRequest(user.name, user.password), session) }
    }

    @Test
    fun `return user on successful register`() {
        every { userRepository.save(user) } returns user

        val userToRegister = UserRequest(user.name, user.password, user.email)
        val registeredUser = authService.register(userToRegister)

        assertThat(registeredUser.name == user.name)
        assertThat(registeredUser.email == user.email)
    }

}