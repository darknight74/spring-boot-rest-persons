package com.fabris.persondemo.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ErrorAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessForbiddenException(): ResponseEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val status = HttpStatus.FORBIDDEN
        return ResponseEntity(
            """
                {
                    "Status": ${status.value()},
                    "Message": "Access forbidden"
                }
            """.trimIndent(), headers, status
        )
    }

    @ExceptionHandler(java.lang.Exception::class)
    fun handleAccessUnauthorizedException(e: Exception): ResponseEntity<Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity(
            """
                {
                    "Status": ${status.value()},
                    "Message": "Internal error"
                }
            """.trimIndent(), headers, status
        )
    }
}