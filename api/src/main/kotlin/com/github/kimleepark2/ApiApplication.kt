package com.github.kimleepark2

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.github.kimleepark2"])
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}