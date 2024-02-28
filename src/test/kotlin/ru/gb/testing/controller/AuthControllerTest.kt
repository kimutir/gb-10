package ru.gb.testing.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.gb.testing.dto.UserRequest
import ru.gb.testing.dto.UserResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.delete
import ru.gb.testing.dto.LoginRequest
import ru.gb.testing.model.User
import ru.gb.testing.repository.SessionRepository
import ru.gb.testing.repository.UserRepository

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
    val userRepository: UserRepository,
    val sessionRepository: SessionRepository
) {

    val baseUrl = "/api/v1/auth"
    private val user = User("Timur", "password", "timur@mail.ru")

    @BeforeAll
    fun setup() {
        val initUser = User(user.name, user.password, user.email)
        mockMvc.post("$baseUrl/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(initUser)
        }
    }

    @Test
    fun `should register new user`() {
        val newUseRequest = UserRequest("${user.name} New", user.password, "${user.email} New")

        mockMvc.post("$baseUrl/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newUseRequest)
        }
            .andExpect {
                status { isCreated() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(UserResponse(2, newUseRequest.name, newUseRequest.email)))
                }
            }

        val registeredUser = userRepository.findByEmail(newUseRequest.email)

        assertThat(registeredUser?.name == newUseRequest.name)
        assertThat(registeredUser?.email == newUseRequest.email)
    }

    @Test
    fun `should login with right credentials`() {
        val loginRequest = LoginRequest(user.name, user.password)

        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }
            .andExpect {
                status { isOk() }
                content {
                    contentType(MediaType.APPLICATION_JSON)
                    json(objectMapper.writeValueAsString(UserResponse(1, user.name, user.email)))
                }
            }

        val loggedUser = userRepository.findByEmail("timur@mail.ru")
        val session = sessionRepository.findByUserId(loggedUser?.id!!)

        assertThat(session != null)
        assertThat(loggedUser.name == user.name)
        assertThat(loggedUser.email == user.email)
    }

    @Test
    fun `should not login with wrong credentials`() {
        val loginRequest = LoginRequest(user.name, "wrong")

        mockMvc.post(baseUrl) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(loginRequest)
        }
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `should successfully logout`() {
        val user = userRepository.findByEmail(user.email)

        mockMvc.delete("$baseUrl/1") {
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }

        val session = sessionRepository.findByUserId(user?.id!!)

        assertThat(session == null)
    }



    @Test
    fun `should not register new user with wrong body`() {
        val userRequest = WrongUserRequest("Timur", "+79999999999")

        mockMvc.post("/api/v1/auth/reg") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userRequest)
        }
            .andExpect {
                status { HttpStatus.BAD_REQUEST }
            }

    }

    private data class WrongUserRequest(
        val name: String,
        val phone: String,
    )
}