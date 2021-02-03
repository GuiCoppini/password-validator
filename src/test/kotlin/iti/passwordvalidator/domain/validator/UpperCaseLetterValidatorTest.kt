package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class UpperCaseLetterValidatorTest {
    val validator = UpperCaseLetterValidator()

    companion object {
        @JvmStatic
        private fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "one upper Case", expected = true, testMessage = "password has a lower case character"),
                TestArgument(password = "ONLY UPPER CASE", expected = true, testMessage = "password has only upper case characters"),
                TestArgument(password = "1 2 3 456", expected = false, testMessage = "password has numbers"),

                TestArgument(password = "lower case password!", expected = false, testMessage = "password fully lower cased"),
                TestArgument(password = "!@#!@#", expected = false, testMessage = "password has only special digits"),
                TestArgument(password = "123234", expected = false, testMessage = "password has only digits")
        )
    }

    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.UPPER_CASE_LETTER_COUNT

        val actualConstraint = validator.constraint

        Assertions.assertEquals(expectedConstraint, actualConstraint)
    }

    @ParameterizedTest
    @MethodSource("passwordAndOutputProvider")
    fun `returns true for valid or false for invalid correctly`(argument: TestArgument) {

        val serviceResult = validator.validate(argument.password)
        Assertions.assertEquals(argument.expected, serviceResult)
    }
}