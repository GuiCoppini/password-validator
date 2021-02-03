package iti.passwordvalidator.rest

import iti.passwordvalidator.common.logging.LogUtils.getLogger
import iti.passwordvalidator.domain.exception.InternalArchitectureException
import iti.passwordvalidator.rest.payload.ErrorPayload
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestExceptionHandler {
    val log = getLogger(this::class.java)

    @ExceptionHandler(InternalArchitectureException::class)
    fun handleInternalArchitectureException(e: InternalArchitectureException): ResponseEntity<ErrorPayload> {
        log.error("InternalArchitectureException raised with message=${e.message}")

        val response = ErrorPayload(500, "Something went wrong during this operation, please contact the developer(s)")
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    // tratando algum erro generico nao tratado pela API
    @ExceptionHandler(Exception::class)
    fun handleGenericException(e: Exception): ResponseEntity<ErrorPayload> {
        log.error("Generic exception raised", e)

        val response = ErrorPayload(500, "Something unexpected happened, please contact the developer(s)")
        return ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR)
    }
}