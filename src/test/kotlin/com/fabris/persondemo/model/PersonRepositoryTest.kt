package com.fabris.persondemo.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@SpringBootTest
class PersonRepositoryTest {

    @Autowired
    lateinit var personRepository: PersonRepository

    @Test
    fun testPersonSave() {
        val name = "Stephen"
        val surname = "Fry"
        val saved = createPerson(
            name = name,
            surname = surname
        )
        assertThat(saved.id).isNotNull
        assertThat(saved.name).isEqualTo(name)
        assertThat(saved.surname).isEqualTo(surname)
    }

    private fun createPerson(
        name: String = "Steve",
        surname: String = "Piglet",
        dateOfBirth: String = "1974-10-25",
        phone: String? = null,
        userName: String = "steve.piglet",
        password: String = "secret3"
    ): Person {
        val person = Person(
            name = name,
            surname = surname,
            dateOfBirth = Date.from(
                LocalDate.parse(dateOfBirth)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
            ),
            phone = phone,
            username = userName,
            password = password
        )

        return personRepository.save(person)
    }

    @Test
    fun testFindAll() {
        val results = personRepository.findAll()
        assertThat(results.count()).isEqualTo(2)
    }

    @Test
    fun testFindPersonById() {
        val found = personRepository.findById(2).orElseThrow()
        assertThat(found.name).isEqualTo("John")
        assertThat(found.surname).isEqualTo("Doe")
        assertThat(found.username).isEqualTo("john.doe")
        assertThat(found.password).isEqualTo("secret2")
    }
}