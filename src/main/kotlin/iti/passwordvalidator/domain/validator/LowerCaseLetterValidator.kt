package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator.Alphabets.LOWER_CASE_LETTERS
import org.springframework.stereotype.Service

@Service
class LowerCaseLetterValidator : Validator {
    override val constraint: Constraint
        get() = Constraint.LOWER_CASE_LETTER_COUNT

    override fun validate(password: String): Boolean =
            password.any {
                it in LOWER_CASE_LETTERS
            }
}