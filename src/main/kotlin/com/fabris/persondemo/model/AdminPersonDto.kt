package com.fabris.persondemo.model

data class AdminPersonDto(
    val id: Long,
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val phone: String?,
    val age: Int,
    val username: String
)
