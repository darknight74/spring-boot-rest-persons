package com.fabris.persondemo.controller

import com.fabris.persondemo.model.AdminPersonDto
import com.fabris.persondemo.model.Person
import com.fabris.persondemo.model.PersonDto
import com.fabris.persondemo.model.toAdminPersonDto
import com.fabris.persondemo.service.AdminPersonService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@RestController
@RequestMapping("/api/admin")
class AdminPersonController (val personService: AdminPersonService){

    @GetMapping("/persons")
    fun getPersons(
        @RequestParam("page") page: Int?,
        @RequestParam("size") size: Int?
    ): List<AdminPersonDto> {
        return personService
            .findAllPersons(page=page, size=size)
            .map { it.toAdminPersonDto() }
    }

    @GetMapping("/persons/{id}")
    fun getPerson(@PathVariable("id") id: Long): ResponseEntity<AdminPersonDto> {
        val foundPerson = personService.findPerson(id)
        return if (foundPerson != null) {
            ResponseEntity.ok(
                foundPerson.toAdminPersonDto()
            )
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PutMapping("/persons/{id}")
    fun updatePerson(@PathVariable("id") id: Long, @RequestBody person: Person): AdminPersonDto {
        return personService.updatePerson(id, person).toAdminPersonDto()
    }

    @DeleteMapping("/persons/{id}")
    fun deletePerson(@PathVariable("id") id: Long): ResponseEntity<Any> {
        return if (personService.deletePerson(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/persons")
    fun createPerson(@RequestBody person: Person): AdminPersonDto {
        return personService.createPerson(person).toAdminPersonDto()
    }

}