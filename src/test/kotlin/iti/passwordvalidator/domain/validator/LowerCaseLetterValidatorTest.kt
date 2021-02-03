package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


class LowerCaseLetterValidatorTest {
    val validator = LowerCaseLetterValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "ONLY UPPER CASE", expected = false, testMessage =  "password has no lower case character"),
                TestArgument(password = "1 2 3 456", expected = false, testMessage =  "password has numbers"),
                TestArgument(password = "!@#!@#", expected = false, testMessage =  "password has special digits"),

                TestArgument(password = "ONE LOWER CASE a", expected = true, testMessage =  "password has a lower case character"),
                TestArgument(password = "lower case password!", expected = true, testMessage =  "password fully lower cased")
        )
    }


    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.LOWER_CASE_LETTER_COUNT

        val actualConstraint= validator.constraint

        Assertions.assertEquals(expectedConstraint, actualConstraint)
    }

    @ParameterizedTest
    @MethodSource("passwordAndOutputProvider")
    fun `returns true for valid or false for invalid correctly`(argument: TestArgument) {

        val serviceResult = validator.validate(argument.password)
        Assertions.assertEquals(argument.expected, serviceResult)
    }
}