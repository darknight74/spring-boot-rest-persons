package com.fabris.persondemo.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ErrorAdvice: ResponseEntityExceptionHandler() {

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessForbiddenException(e: AccessDeniedException, r: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(e, null, HttpHeaders(), HttpStatus.FORBIDDEN, r)
    }

    @ExceptionHandler(Exception::class)
    fun handleAccessUnauthorizedException(e: Exception, r: WebRequest): ResponseEntity<Any> {
        return handleExceptionInternal(e, null, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, r)
    }

    override fun handleExceptionInternal(
        ex: java.lang.Exception,
        body: Any?,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        logger.error("Caught error when accessing ${request.getDescription(false)} user ${request.userPrincipal?.name}", ex)
        headers.contentType = MediaType.APPLICATION_JSON
        val redefinedBody = mapOf("Status" to status.value(), "Message" to status.reasonPhrase)
        return super.handleExceptionInternal(ex, redefinedBody, headers, status, request)
    }
}