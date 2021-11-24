package com.fabris.persondemo.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import javax.transaction.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActuatorTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun testCountActuator() {
        mockMvc
            .get("/actuator/persons") {
                contentType = MediaType.APPLICATION_JSON
                accept = MediaType.APPLICATION_JSON
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.count", Matchers.`is`(2))
            }
    }
}