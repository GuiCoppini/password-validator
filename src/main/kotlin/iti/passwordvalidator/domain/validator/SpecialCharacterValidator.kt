package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator.Alphabets.ALLOWED_SPECIAL_CHARACTERS
import org.springframework.stereotype.Service

@Service
class SpecialCharacterValidator : Validator {
    override val constraint: Constraint
        get() = Constraint.SPECIAL_CHARACTER

    override fun validate(password: String): Boolean =
            password.any {
                it in ALLOWED_SPECIAL_CHARACTERS
            }
}