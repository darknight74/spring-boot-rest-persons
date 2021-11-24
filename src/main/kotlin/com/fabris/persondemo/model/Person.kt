package com.fabris.persondemo.model

import java.time.LocalDate
import java.time.Period
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "person")
data class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val surname: String,
    val phone: String? = null,
    val dateOfBirth: Date,
    val username: String,
    val password: String
) {
    fun getAge(): Int {
        return Period.between(
            dateOfBirth.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            LocalDate.now()
        ).years
    }
}

fun Person.toAdminPersonDto(): AdminPersonDto {
    val dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")

    return AdminPersonDto(
        id = id,
        name = name,
        surname = surname,
        phone = phone,
        dateOfBirth = dateOfBirth.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(dateFormatter),
        username = username,
        age = getAge()
    )
}

fun Person.toPersonDto(): PersonDto {
    val dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-dd")

    return PersonDto(
        id = id,
        name = name,
        surname = surname,
        phone = phone,
        dateOfBirth = dateOfBirth.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .format(dateFormatter),
        age = getAge()
    )
}
