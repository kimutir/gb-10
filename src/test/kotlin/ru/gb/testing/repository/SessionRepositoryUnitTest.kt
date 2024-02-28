package ru.gb.testing.repository

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.gb.testing.model.Session
import ru.gb.testing.model.User

@DataJpaTest
class SessionRepositoryUnitTest {

    @Autowired
    lateinit var sessionRepository: SessionRepository

    val user = User("Timur", "123", "m@mail.ru")

    @BeforeEach
    fun setup() {
        sessionRepository.save(Session(user))
    }

    @Test
    fun `returns session by user id`() {
        val session = sessionRepository.findByUserId(1)
        assertThat(session != null)
    }

}