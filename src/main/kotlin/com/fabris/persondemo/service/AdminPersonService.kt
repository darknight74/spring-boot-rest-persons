package com.fabris.persondemo.service

import com.fabris.persondemo.model.Person
import com.fabris.persondemo.model.PersonRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
@PreAuthorize("hasRole('ADMIN')")
class AdminPersonService(val personRepository: PersonRepository, val passwordEncoder: PasswordEncoder) {


    fun findAllPersons(page: Int?, size: Int?): List<Person> {
        return personRepository
            .findAll(Pageable.ofSize(size?:5).withPage(page?:0))
            .content
    }

    fun findPerson(id: Long): Person? {
        val optionalPerson = personRepository.findById(id)
        return if (optionalPerson.isPresent) {
            optionalPerson.get()
        } else {
            null
        }
    }

    fun createPerson(person: Person): Person {
        val personCreate = Person(
            name = person.name,
            surname = person.surname,
            dateOfBirth = person.dateOfBirth,
            phone = person.phone,
            username = person.username,
            password = passwordEncoder.encode(person.password)
        )
        return personRepository.save(personCreate)
    }

    fun updatePerson(id: Long, person: Person): Person {
        val personUpdate = Person(
            id = id,
            name = person.name,
            surname = person.surname,
            dateOfBirth = person.dateOfBirth,
            username = person.username,
            password = passwordEncoder.encode(person.password)
        )

        return personRepository.save(personUpdate)
    }

    fun deletePerson(id: Long): Boolean {
        return try {
            personRepository.deleteById(id)
            true
        } catch (e: EmptyResultDataAccessException) {
            false
        }
    }
}