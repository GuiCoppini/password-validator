package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class MinimumCharactersValidatorTest {
    val validator = MinimumCharactersValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "lessThn9", expected = false, testMessage =  "password has less than 9 characters"),
                TestArgument(password = "12345678", expected = false, testMessage =  "password has less than 9 characters with only numbers"),
                TestArgument(password = "!#$&", expected = false, testMessage =  "password has less than 9 characters with only specials"),

                TestArgument(password = "moreThan9Characters!!!", expected = true, testMessage =  "password has more than nine characters"),
                TestArgument(password = "morethannine", expected = true, testMessage =  "password has more than nine characters only letters"),
                TestArgument(password = "123asd!@#", expected = true, testMessage =  "password has exatcly nine characterrs")
        )
    }


    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.MINIMUM_SIZE

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