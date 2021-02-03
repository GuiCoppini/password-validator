package iti.passwordvalidator.domain.service

import io.mockk.every
import io.mockk.mockk
import iti.passwordvalidator.common.exception.InternalArchitectureException
import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test


internal class ValidatorFactoryImplTest {
    @Test
    internal fun `Checks constraint from sent validators`() {
        val firstValidator = mockk<Validator>()
        val firstValConstraint = mockk<Constraint>()
        every { firstValidator.constraint } answers { firstValConstraint }

        val secondValidator = mockk<Validator>()
        val secondValConstraint = mockk<Constraint>()
        every { secondValidator.constraint } answers { secondValConstraint }

        val thirdValidator = mockk<Validator>()
        val thirdValConstraint = mockk<Constraint>()
        every { thirdValidator.constraint } answers { thirdValConstraint }

        val mockedValidators: List<Validator> = listOf(firstValidator, secondValidator, thirdValidator)

        val factory = ValidatorFactoryImpl(mockedValidators)

        val actualFirstValidator = factory.getValidatorFor(firstValConstraint)
        val actualSecondValidator = factory.getValidatorFor(secondValConstraint)
        val actualThirdValidator = factory.getValidatorFor(thirdValConstraint)

        // assert que retornou o validator que estava apontando para aquela constraint
        assertEquals(firstValidator, actualFirstValidator)
        assertEquals(secondValidator, actualSecondValidator)
        assertEquals(thirdValidator, actualThirdValidator)
    }

    @Test
    internal fun `Throws InternalArchitectureException on unknown constraint`() {
        val validatorMock = mockk<Validator>()
        // o validator usa uma constraint que nao eh a mesma usada pra buscar um validator da lista
        val validatorConstraint = mockk<Constraint>()
        every { validatorMock.constraint } answers { validatorConstraint }

        val unknownConstraint = mockk<Constraint>()

        val mockedValidators: List<Validator> = listOf(validatorMock)

        // lista com validator que nao possui constraint que sera usada
        val factory = ValidatorFactoryImpl(mockedValidators)


        // vai procurar algum validator que tem a unknownConstraint e nenhum validator tera
        assertThrows(InternalArchitectureException::class.java) {
            factory.getValidatorFor(unknownConstraint)
        }
    }
}