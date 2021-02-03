package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class InvalidCharacterValidatorTest {
    val validator = InvalidCharacterValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "password+without+invalid", expected = true, testMessage =  "password has no invalid character"),
                TestArgument(password = "password!without!invalid", expected = true, testMessage = "password has no invalid character"),
                TestArgument(password = "passwordWithNumbersAndLetters1123", expected = true, testMessage = "password with normal characters"),

                TestArgument(password = "password with spaces", expected = false, testMessage = "password with spaces"),
                TestArgument(password = "password.with....", expected = false, testMessage = "password with dots"),
                TestArgument(password = "password+with+\"", expected = false, testMessage = "password with double quotes"),
                TestArgument(password = "password+with+'", expected = false, testMessage = "password with single quotes"),
                TestArgument(password = "password+with+~", expected = false, testMessage = "password with tilde")
        )

    }

    @ParameterizedTest
    @MethodSource("passwordAndOutputProvider")
    fun `returns false only for passwords with invalid characters`(argument: TestArgument) {

        val serviceResult = validator.validate(argument.password)
        assertEquals(argument.expected, serviceResult, argument.testMessage)
    }

    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.INVALID_CHARACTER

        val actualConstraint = validator.constraint

        assertEquals(expectedConstraint, actualConstraint)
    }

}