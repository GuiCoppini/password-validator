package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import org.springframework.stereotype.Service

@Service
class NumberCountValidator : Validator {
    override val constraint: Constraint
        get() = Constraint.DIGITS_COUNT

    // regex para ver se a string contem algum numero
    override fun validate(password: String) =
            password
                    .any { it.isDigit() }
}