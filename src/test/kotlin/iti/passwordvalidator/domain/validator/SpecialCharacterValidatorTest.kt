package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.TestArgument
import iti.passwordvalidator.domain.model.Constraint
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SpecialCharacterValidatorTest {
    val validator = SpecialCharacterValidator()

    private companion object {
        @JvmStatic
        fun passwordAndOutputProvider() = Stream.of(
                TestArgument(password = "tantofaz !!tantofaz", expected = true, testMessage = "password has ! character"),
                TestArgument(password = "tantofaz @@tantofaz", expected = true, testMessage = "password has @ character"),
                TestArgument(password = "tantofaz ##tantofaz", expected = true, testMessage = "password has # character"),
                TestArgument(password = "tantofaz \$\$tantofaz", expected = true, testMessage = "password has $ character"),
                TestArgument(password = "tantofaz %%tantofaz", expected = true, testMessage = "password has % character"),
                TestArgument(password = "tantofaz ^^tantofaz", expected = true, testMessage = "password has ^ character"),
                TestArgument(password = "tantofaz &&tantofaz", expected = true, testMessage = "password has & character"),
                TestArgument(password = "tantofaz **tantofaz", expected = true, testMessage = "password has * character"),
                TestArgument(password = "tantofaz ((tantofaz", expected = true, testMessage = "password has ( character"),
                TestArgument(password = "tantofaz ))tantofaz", expected = true, testMessage = "password has ) character"),
                TestArgument(password = "tantofaz ++tantofaz", expected = true, testMessage = "password has + character"),
                TestArgument(password = "tantofaz --tantofaz", expected = true, testMessage = "password has - character"),
                TestArgument(password = "--", expected = true, testMessage = "password has double special character"),
                TestArgument(password = "--", expected = true, testMessage = "password has only one special character"),


                TestArgument(password = "tantofaz", expected = false, testMessage = "password has only letters"),
                TestArgument(password = "123123", expected = false, testMessage = "password has only numbers"),
                TestArgument(password = "   ", expected = false, testMessage = "password has only spaces")
        )
    }

        @Test
        internal fun `returns correct constraint`() {
            val expectedConstraint = Constraint.SPECIAL_CHARACTER

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


