package com.fabris.persondemo.service

import com.fabris.persondemo.model.PersonRepository
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Service

@Service
@WebEndpoint(id = "persons")
class CustomActuator(val personRepository: PersonRepository) {

    @ReadOperation
    fun personsCount(): Map<String, Any> {
        return mapOf("count" to personRepository.count())
    }
}