package com.fabris.persondemo.service

import com.fabris.persondemo.model.Person
import com.fabris.persondemo.model.PersonRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
@PreAuthorize("hasRole('USER')")
class PersonService(val personRepository: PersonRepository) {

    fun getPersonsByNameAndAge(name: String, age: Int): List<Person> {
        return personRepository.findAllByName(name).filter { it.getAge() == age }
    }
}