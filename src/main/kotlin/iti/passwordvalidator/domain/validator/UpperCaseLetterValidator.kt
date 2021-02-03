package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator.Alphabets.UPPER_CASE_LETTERS
import org.springframework.stereotype.Service

@Service
class UpperCaseLetterValidator : Validator {
    override val constraint: Constraint
        get() = Constraint.UPPER_CASE_LETTER_COUNT

    override fun validate(password: String): Boolean =
            password.any {
                it in UPPER_CASE_LETTERS
            }
}