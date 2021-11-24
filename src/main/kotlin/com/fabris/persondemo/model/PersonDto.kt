package com.fabris.persondemo.model

import java.util.*

data class PersonDto(
    val id: Long,
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val age: Int,
    val phone: String?
)
