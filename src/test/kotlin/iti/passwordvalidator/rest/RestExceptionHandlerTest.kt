package iti.passwordvalidator.rest

import iti.passwordvalidator.domain.exception.InternalArchitectureException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpMediaTypeNotSupportedException

internal class RestExceptionHandlerTest {

    val handler = RestExceptionHandler()

    @Test
    internal fun `InternalArchitectureException should return 500`() {
        val exceptionMessage = "ORIGINAL MESSAGE"
        val thrownExc = InternalArchitectureException(exceptionMessage)

        val response = handler.handleInternalArchitectureException(thrownExc)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.body!!.code)

        // nao populou o body com a mensagem da exception
        assertNotEquals(exceptionMessage, response.body!!.message)
    }

    @Test
    internal fun `Generic Exception should return 500`() {
        val exceptionMessage = "ORIGINAL MESSAGE"
        val thrownExc = Exception(exceptionMessage)

        val response = handler.handleGenericException(thrownExc)

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.statusCode)
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.body!!.code)

        // nao populou o body com a mensagem da exception
        assertNotEquals(exceptionMessage, response.body!!.message)
    }

    @Test
    internal fun `HttpMessageException should return 400`() {
        val exceptionMessage = "ORIGINAL MESSAGE"
        val thrownExc = HttpMessageNotReadableException(exceptionMessage)

        val response = handler.handleHttpMessageErrorException(thrownExc)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.body!!.code)

        // nao populou o body com a mensagem da exception
        assertNotEquals(exceptionMessage, response.body!!.message)
    }

    @Test
    internal fun `HttpMediaTypeNotSupportedException should return 400`() {
        val exceptionMessage = "ORIGINAL MESSAGE"
        val thrownExc = HttpMediaTypeNotSupportedException(exceptionMessage)

        val response = handler.handleHttpMessageErrorException(thrownExc)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.body!!.code)

        // nao populou o body com a mensagem da exception
        assertNotEquals(exceptionMessage, response.body!!.message)
    }

}