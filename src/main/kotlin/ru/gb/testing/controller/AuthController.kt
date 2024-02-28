package ru.gb.testing.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.gb.testing.dto.LoginRequest
import ru.gb.testing.dto.UserRequest
import ru.gb.testing.exception.UserNotFoundException
import ru.gb.testing.service.AuthService


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(e: UserNotFoundException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @PostMapping("/reg")
    @ResponseStatus(HttpStatus.CREATED)
     fun register(@RequestBody request: UserRequest) = authService.register(request)

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
     fun login(@RequestBody request: LoginRequest) = authService.login(request)

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
     fun logout(@PathVariable userId: Int) = authService.logout(userId)
}