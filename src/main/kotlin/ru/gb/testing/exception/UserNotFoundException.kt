package ru.gb.testing.exception

class UserNotFoundException(
    msg: String
): RuntimeException(msg)