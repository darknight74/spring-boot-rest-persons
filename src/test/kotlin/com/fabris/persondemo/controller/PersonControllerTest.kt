package com.fabris.persondemo.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDate
import java.time.Period
import javax.transaction.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PersonControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val apiPath = "/api/persons"

    @Test
    fun testGetPersonsByNameAndAgeWithNoUser() {
        val calculatedAge = Period.between(LocalDate.of(1974, 10, 25), LocalDate.now()).years
        mockMvc
            .get(apiPath) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                param("name", "Johanna")
                param("age",  calculatedAge.toString())
            }.andDo {
                print()
            }.andExpect {
                status { isUnauthorized() }
            }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testGetPersonsByNameAndAgeWithUser() {
        val calculatedAge = Period.between(LocalDate.of(1974, 10, 25), LocalDate.now()).years
        mockMvc
            .get(apiPath) {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
                param("name", "Johanna")
                param("age",  calculatedAge.toString())
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isArray
                jsonPath<Collection<Any>>("$", Matchers.hasSize(1))
                jsonPath("$[0].id", Matchers.`is`(1))
                jsonPath("$[0].name", Matchers.`is`("Johanna"))
                jsonPath("$[0].phone", Matchers.`is`("xxxxx"))
                jsonPath("$[0].username").doesNotExist()
                jsonPath("$[0].password").doesNotExist()
            }
    }
}