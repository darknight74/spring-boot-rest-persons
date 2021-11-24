package com.fabris.persondemo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonDemoApplication

fun main(args: Array<String>) {
    runApplication<PersonDemoApplication>(*args)
}
