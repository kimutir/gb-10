package ru.gb.testing

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GbTestingApplication

fun main(args: Array<String>) {
    runApplication<GbTestingApplication>(*args)
}
