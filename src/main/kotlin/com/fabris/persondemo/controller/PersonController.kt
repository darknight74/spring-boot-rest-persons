package com.fabris.persondemo.controller

import com.fabris.persondemo.model.PersonDto
import com.fabris.persondemo.model.toPersonDto
import com.fabris.persondemo.service.PersonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class PersonController(val personService: PersonService) {

    @GetMapping("/persons")
    fun getPersons(@RequestParam("name") name: String, @RequestParam("age") age: Int): List<PersonDto> {
        return personService.getPersonsByNameAndAge(name, age)
            .map {
                it.toPersonDto()
            }
    }
}