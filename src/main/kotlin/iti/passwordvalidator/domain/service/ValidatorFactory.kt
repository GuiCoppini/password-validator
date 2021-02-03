package iti.passwordvalidator.domain.service

import iti.passwordvalidator.common.logging.LogUtils.getLogger
import iti.passwordvalidator.domain.exception.InternalArchitectureException
import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator
import org.springframework.stereotype.Service

interface ValidatorFactory {
    fun getValidatorFor(constraint: Constraint) : Validator
}

@Service
class ValidatorFactoryImpl(val validators: List<Validator>) : ValidatorFactory {
    val log = getLogger(this::class.java)

    override fun getValidatorFor(constraint: Constraint): Validator {
        validators.forEach {
            if(it.constraint == constraint) return it
        }

        log.error("No validator found for constraint, raising InternalArchitectureException. constraint=$constraint")
        throw InternalArchitectureException("No validator found for constraint")
    }
}