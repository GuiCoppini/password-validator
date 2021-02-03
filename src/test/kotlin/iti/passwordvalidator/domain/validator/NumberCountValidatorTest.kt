package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class NumberCountValidatorTest {
    val validator = NumberCountValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "has 1 number!", expected = true, testMessage =  "password has one digit character"),
                TestArgument(password = "123123", expected = true, testMessage =  "password is only digits"),
                TestArgument(password = "has no number!", expected = false, testMessage =  "password has no digit character")
        )
    }

    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.DIGITS_COUNT

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