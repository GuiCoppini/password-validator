package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import org.springframework.stereotype.Service

@Service
class MinimumCharactersValidator : Validator {
    private val MINIMUM_PASSWORD_SIZE = 9

    override val constraint: Constraint
        get() = Constraint.MINIMUM_SIZE

    override fun validate(password: String): Boolean {
        return password.length >= MINIMUM_PASSWORD_SIZE
    }
}