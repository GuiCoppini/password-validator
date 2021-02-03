package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class RepeatingCharacterValidatorTest {
    val validator = RepeatingCharacterValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "no repatig", expected = true, testMessage =  "password has no digit character"),
                TestArgument(password = "aBcb", expected = true, testMessage =  "password has same letter on lower and upper case"),

                TestArgument(password = "tyuio(aa)vbnm", expected = false, testMessage =  "password has double 'a'"),
                TestArgument(password = "aba", expected = false, testMessage =  "password has 'a' repeating after a letter"),
                TestArgument(password = "!rep!", expected = false, testMessage =  "password has a repeating special"),
                TestArgument(password = "aBcB", expected = false, testMessage =  "password has a repeating upper case")
        )
    }

    @Test
    internal fun `returns correct constraint`() {
        val expectedConstraint = Constraint.NO_REPEATING_CHARACTER

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