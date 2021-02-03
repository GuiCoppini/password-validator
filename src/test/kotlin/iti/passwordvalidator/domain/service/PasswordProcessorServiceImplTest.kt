package iti.passwordvalidator.domain.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class PasswordProcessorServiceImplTest {
    val factoryMock = mockk<ValidatorFactory>()
    val processor = PasswordProcessorServiceImpl(factoryMock)


    @Test
    internal fun `Gets validator from factory based on constraint used`() {
        val fakeValidator = mockk<Validator>()


        val usedConstraints = listOf(
                Constraint.NO_REPEATING_CHARACTER,
                Constraint.SPECIAL_CHARACTER
        )

        every { factoryMock.getValidatorFor(any()) } answers { fakeValidator }

        // vou testar em outro metodo, soh esta aqui para nao dar erro
        every { fakeValidator.validate(any()) } answers { true }

        processor.process("anything", usedConstraints)

        // asserta que chama o validatorFor para cada constraint usada, garantindo que vao usar o service correto
        usedConstraints.forEach { usedConstraint ->
            verify(exactly = 1) { factoryMock.getValidatorFor(usedConstraint) }
        }
    }

    @Test
    internal fun `Calls validate method from validator returned from factory`() {
        val expectedValidator = mockk<Validator>()
        val usedPassword = "anything"


        every { factoryMock.getValidatorFor(any()) } answers { expectedValidator }

        // tanto faz a resposta, vou testar em outro metodo, coloquei aqui para nao dar erro
        every { expectedValidator.validate(usedPassword) } answers { true }

        processor.process(usedPassword, listOf(Constraint.INVALID_CHARACTER))

        verify(exactly = 1) { expectedValidator.validate(any()) }
    }

    @Test
    internal fun `Returns false if any validator returns false`() {
        val trueValidator = mockk<Validator>()
        val falseValidator = mockk<Validator>()

        val usedPassword = "anything"

        // vai dar false, true, true
        every { factoryMock.getValidatorFor(any()) } returnsMany  listOf(trueValidator, falseValidator, trueValidator)

        every { trueValidator.validate(usedPassword) } answers { true }
        every { falseValidator.validate(usedPassword) } answers { false }

        // 3 constraints para retornar os 3 validators
        val actualResult = processor.process(usedPassword, listOf(Constraint.INVALID_CHARACTER, Constraint.UPPER_CASE_LETTER_COUNT, Constraint.SPECIAL_CHARACTER))

        assertFalse(actualResult)
    }

    @Test
    internal fun `Returns true if all validators return true`() {
        val trueValidator = mockk<Validator>()

        val usedPassword = "anything"

        // vai dar false, true, true
        every { factoryMock.getValidatorFor(any()) } answers { trueValidator }

        every { trueValidator.validate(usedPassword) } answers { true }

        val actualResult = processor.process(usedPassword, listOf(Constraint.INVALID_CHARACTER))

        verify(exactly = 1) { trueValidator.validate(usedPassword) }

        assertTrue(actualResult)
    }
}