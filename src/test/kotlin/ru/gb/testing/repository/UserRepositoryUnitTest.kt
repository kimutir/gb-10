package ru.gb.testing.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.findByIdOrNull
import ru.gb.testing.model.User

@DataJpaTest
class UserRepositoryUnitTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setup() {
        val user = User("Timur", "123", "m@mail.ru")
        userRepository.save(user)
    }

    @Test
    fun `returns user by id`() {
        val user = userRepository.findByIdOrNull(1)
        assertThat(user?.name == "Timur")
    }
    @Test
    fun `returns users by email`() {
        val user = userRepository.findByEmail("m@mail.ru")
        assertThat(user?.name == "Timur")
    }
    @Test
    fun `returns null by wrong id`() {
        val user = userRepository.findByIdOrNull(100)
        assertThat(user == null)
    }
    @Test
    fun `deletes user by id`() {
        userRepository.deleteById(1)
        val users = userRepository.findAll()

        assertThat(users.size == 0)
    }

}