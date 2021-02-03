package iti.passwordvalidator.rest

import iti.passwordvalidator.common.logging.LogUtils.getLogger
import iti.passwordvalidator.domain.exception.InternalArchitectureException
import iti.passwordvalidator.rest.payload.ErrorPayload
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException
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

    @ExceptionHandler(HttpMessageNotReadableException::class, HttpMediaTypeNotSupportedException::class)
    fun handleHttpMessageErrorException(e: Exception): ResponseEntity<ErrorPayload> {
        log.error("Error while reading HTTP Message, this was a bad request", e)

        val response = ErrorPayload(400, "Error while reading HTTP message, please check your request")
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }
}