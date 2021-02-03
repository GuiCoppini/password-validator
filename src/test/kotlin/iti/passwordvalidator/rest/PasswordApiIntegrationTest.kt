package iti.passwordvalidator.rest

import iti.passwordvalidator.PasswordValidatorApplication
import iti.passwordvalidator.rest.payload.ErrorPayload
import iti.passwordvalidator.rest.payload.ValidateRequest
import iti.passwordvalidator.rest.payload.ValidateResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [PasswordValidatorApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class PasswordApiIntegrationTest {
    @Autowired
    lateinit var client: TestRestTemplate

    @Test
    internal fun `Returns ErrorPayload and 400 when request has null password`() {
        val request = """{
                "password" : null
            }""".trimMargin()

        val validationResult = client.postForEntity<ErrorPayload>("/password/validate", request)

        assertEquals(HttpStatus.BAD_REQUEST.value(), validationResult.body!!.code)
        assertEquals(HttpStatus.BAD_REQUEST, validationResult.statusCode)
    }

    @Test
    internal fun `Returns ErrorPayload and 400 when request is an empty JSON`() {
        val request = "{}"

        val validationResult = client.postForEntity<ErrorPayload>("/password/validate", request)

        assertEquals(HttpStatus.BAD_REQUEST.value(), validationResult.body!!.code)
        assertEquals(HttpStatus.BAD_REQUEST, validationResult.statusCode)
    }

    @Test
    internal fun `Returns false when password breaks minimum size constraint`() {
        val smallPassword = "1!AbcD"
        val request = ValidateRequest(smallPassword)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password breaks minimum digits constraint`() {
        val noDigitsPass = "NoDigts!#@%"
        val request = ValidateRequest(noDigitsPass)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password breaks lower case constraint`() {
        val noLowerCase = "NO+LWR!@#123"
        val request = ValidateRequest(noLowerCase)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password breaks minimum upper case constraint`() {
        val noUpperCasePass = "no+upr!@#123"
        val request = ValidateRequest(noUpperCasePass)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password breaks special characters constraint`() {
        val noSpecialCharPass = "noSpec12345"
        val request = ValidateRequest(noSpecialCharPass)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password breaks repeatable characters constraint`() {
        val repeatingCPassword = "Ab(CC)d123!@#"
        val request = ValidateRequest(repeatingCPassword)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password has a space character`() {
        val spacedPassword = "NiceBut1! " // tem um espaco
        val request = ValidateRequest(spacedPassword)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns false when password has a generically invalid character`() {
        val dottedPassword = "NiceBut1!." // tem um ponto
        val request = ValidateRequest(dottedPassword)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertFalse(validationResult.body!!.valid)
    }

    @Test
    internal fun `Returns true when password fulfills all constraints`() {
        val perfectPassword = "PerfCt!123"
        val request = ValidateRequest(perfectPassword)

        val validationResult = client.postForEntity<ValidateResponse>("/password/validate", request)

        assertTrue(validationResult.body!!.valid)
    }
}