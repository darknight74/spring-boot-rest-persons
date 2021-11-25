package com.fabris.persondemo.controller

import com.fabris.persondemo.model.AdminPersonDto
import com.fabris.persondemo.model.PersonRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDate
import java.time.Period
import javax.transaction.Transactional


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AdminPersonControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var personRepository: PersonRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    private val apiPath = "/api/admin/persons"

    val johannaAge = Period.between(
        LocalDate.of(1974, 10, 25),
        LocalDate.now()
    ).years

    private val johnAge = Period.between(
        LocalDate.of(1976, 10, 26),
        LocalDate.now(),
    ).years

    @Test
    fun testGetPersonsWithNoUser() {
        mockMvc
            .get(apiPath)
            .andDo {
                print()
            }.andExpect {
                checkAccessUnauthorized()
            }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testGetPersonsWithUser() {
        mockMvc
            .get(apiPath) {
                setJsonHeaders()
            }
            .andDo{
                print()
            }.andExpect {
                checkAccessForbidden()
            }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testGetPersonsWithAdmin() {
        mockMvc
            .get(apiPath) {
                setJsonHeaders()
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isArray
                jsonPath<Collection<Any>>("$", hasSize(2))
                jsonPath("$[0].id", Matchers.`is`(1))
                jsonPath("$[0].name", Matchers.`is`("Johanna"))
                jsonPath("$[0].dateOfBirth", Matchers.`is`("1974-10-25"))
                jsonPath("$[0].age", Matchers.`is`(johannaAge))
                jsonPath("$[0].username", "johanna.doe")
                jsonPath("$[0].phone", "xxxx")
                jsonPath("$[0].password").doesNotExist()
                jsonPath("$[1].id", Matchers.`is`(2))
                jsonPath("$[1].name", Matchers.`is`("John"))
                jsonPath("$[1].dateOfBirth", Matchers.`is`("1976-10-26"))
                jsonPath("$[1].age", Matchers.`is`(johnAge))
                jsonPath("$[1].username", "john.doe")
                jsonPath("$[1].password").doesNotExist()
                jsonPath("$[1].phone", "yyyy")
            }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testGetPersonsWithPaginationWithAdmin() {
        mockMvc
            .get(apiPath) {
                setJsonHeaders()
                param("page", "1")
                param("size", "1")
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isArray
                jsonPath<Collection<Any>>("$", hasSize(1))
                jsonPath("$[0].id", Matchers.`is`(2))
                jsonPath("$[0].name", Matchers.`is`("John"))
                jsonPath("$[0].dateOfBirth", Matchers.`is`("1976-10-26"))
                jsonPath("$[0].age", Matchers.`is`(johnAge))
                jsonPath("$[0].username", "john.doe")
                jsonPath("$[0].password").doesNotExist()
                jsonPath("$[0].phone", Matchers.`is`("yyyyy"))
            }
    }

    @Test
    fun testGetPersonWithNoUser() {
        mockMvc
            .get("$apiPath/2")
            .andDo {
                print()
            }.andExpect {
                checkAccessUnauthorized()
            }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testGetPersonWithUser() {
        mockMvc
            .get("$apiPath/2")
            .andDo{
                print()
            }.andExpect {
                checkAccessForbidden()
            }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testGetPersonWithAdminExistingUser() {
        mockMvc
            .get("$apiPath/2") {
                setJsonHeaders()
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isNotEmpty
                jsonPath("$.id", Matchers.`is`(2))
                jsonPath("$.name", Matchers.`is`("John"))
                jsonPath("$.dateOfBirth", Matchers.`is`("1976-10-26"))
                jsonPath("$.age", Matchers.`is`(johnAge))
                jsonPath("$.username", "john.doe")
                jsonPath("$.password").doesNotExist()
            }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testGetPersonWithAdminNonExistingUser() {
        mockMvc
            .get("$apiPath/3") {
                setJsonHeaders()
            }.andDo {
                print()
            }.andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun testCreatePersonWithNoUser() {
        mockMvc.post(apiPath) {
            content =  """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password"
                    }
                """.trimIndent()
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessUnauthorized()
        }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testCreatePersonWithUser() {
        mockMvc.post(apiPath) {
            content =  """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password",
                        "dateOfBirth": "1982-01-26"
                    }
                """.trimIndent()
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessForbidden()
        }
    }

    private fun MockMvcResultMatchersDsl.checkAccessForbidden() {
        status { isForbidden() }
        content { contentType(MediaType.APPLICATION_JSON) }
        jsonPath("$.Status", Matchers.`is`(HttpStatus.FORBIDDEN.value()))
        jsonPath("$.Message", Matchers.`is`(HttpStatus.FORBIDDEN.reasonPhrase))
    }

    private fun MockMvcResultMatchersDsl.checkAccessUnauthorized() {
        status { isUnauthorized() }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testCreatePersonWithAdmin() {
        val expectedAge = Period.between(
            LocalDate.of(1982, 1, 26),
            LocalDate.now(),
        ).years

        val result = mockMvc
            .post(apiPath) {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password",
                        "dateOfBirth": "1982-01-26"
                    }
                """.trimIndent()
                accept = MediaType.APPLICATION_JSON
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isNotEmpty
                jsonPath("$.id").isNotEmpty
                jsonPath("$.name", Matchers.`is`("New"))
                jsonPath("$.surname", Matchers.`is`("User"))
                jsonPath("$.dateOfBirth", Matchers.`is`("1982-01-26"))
                jsonPath("$.age", Matchers.`is`(expectedAge))
                jsonPath("$.username", "new.user")
                jsonPath("$.password").doesNotExist()
                jsonPath("$.phone").isEmpty
            }.andReturn()

            val createdPerson = objectMapper.readValue(result.response.contentAsString, AdminPersonDto::class.java)

            val retrievedPerson = personRepository.findById(createdPerson.id)
            assertTrue(retrievedPerson.isPresent)
            assertTrue(passwordEncoder.matches("password", retrievedPerson.get().password))
    }

    @Test
    fun testUpdatePersonWithNoUser() {
        mockMvc.put("$apiPath/1") {
            content =  """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password"
                    }
                """.trimIndent()
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessUnauthorized()
        }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testUpdatePersonWithUser() {
        mockMvc.put("$apiPath/1") {
            content =  """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password",
                        "dateOfBirth": "1982-01-26"
                    }
                """.trimIndent()
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessForbidden()
        }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testUpdatePersonWithAdmin() {
        val expectedAge = Period.between(
            LocalDate.of(1982, 1, 26),
            LocalDate.now(),
        ).years

        mockMvc
            .put("$apiPath/1") {
                contentType = MediaType.APPLICATION_JSON
                content = """
                    {
                        "name": "New",
                        "surname": "User",
                        "username": "user.name",
                        "password": "password",
                        "dateOfBirth": "1982-01-26"
                    }
                """.trimIndent()
                accept = MediaType.APPLICATION_JSON
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
                jsonPath("$").isNotEmpty
                jsonPath("$.id").isNotEmpty
                jsonPath("$.id").value(1)
                jsonPath("$.name", Matchers.`is`("New"))
                jsonPath("$.surname", Matchers.`is`("User"))
                jsonPath("$.dateOfBirth", Matchers.`is`("1982-01-26"))
                jsonPath("$.age", Matchers.`is`(expectedAge))
                jsonPath("$.username", "new.user")
                jsonPath("$.password").doesNotExist()
                jsonPath("$.phone").isEmpty
            }.andReturn()

        val retrievedPerson = personRepository.findById(1)
        assertTrue(retrievedPerson.isPresent)
        assertTrue(passwordEncoder.matches("password", retrievedPerson.get().password))
    }

    @Test
    fun testDeletePersonWithNoUser() {
        mockMvc.delete("$apiPath/1") {
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessUnauthorized()
        }
    }

    @WithMockUser(username="test", roles = ["USER"])
    @Test
    fun testDeletePersonWithUser() {
        mockMvc.delete("$apiPath/1") {
            setJsonHeaders()
        }.andDo {
            print()
        }.andExpect {
            checkAccessForbidden()
        }
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testDeletePersonWithAdmin() {
        mockMvc
            .delete("$apiPath/1") {
                setJsonHeaders()
            }.andDo {
                print()
            }.andExpect {
                status { isOk() }
            }.andReturn()

        val retrievedPersons = personRepository.findAll()
        assertEquals(1, retrievedPersons.count())
    }

    @WithMockUser(username="test", roles = ["ADMIN"])
    @Test
    fun testDeletePersonWithAdminNonExistingResource() {
        mockMvc
            .delete("$apiPath/3") {
                setJsonHeaders()
            }.andDo {
                print()
            }.andExpect {
                status { isNotFound() }
            }.andReturn()

        val retrievedPersons = personRepository.findAll()
        assertEquals(2, retrievedPersons.count())
    }

    private fun MockHttpServletRequestDsl.setJsonHeaders() {
        contentType = MediaType.APPLICATION_JSON
        accept = MediaType.APPLICATION_JSON
    }
}