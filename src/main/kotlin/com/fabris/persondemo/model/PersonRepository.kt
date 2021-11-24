package com.fabris.persondemo.model

import org.springframework.data.repository.PagingAndSortingRepository

interface PersonRepository: PagingAndSortingRepository<Person, Long> {
    fun findAllByName(name: String): List<Person>
}