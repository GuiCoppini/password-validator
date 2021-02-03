package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import iti.passwordvalidator.domain.validator.Validator.Alphabets.ALLOWED_SPECIAL_CHARACTERS
import iti.passwordvalidator.domain.validator.Validator.Alphabets.LOWER_CASE_LETTERS
import iti.passwordvalidator.domain.validator.Validator.Alphabets.UPPER_CASE_LETTERS
import org.springframework.stereotype.Service

@Service
class InvalidCharacterValidator : Validator {
    override val constraint: Constraint
        get() = Constraint.INVALID_CHARACTER

    override fun validate(password: String): Boolean {
        // caracteres invalidos serao todos que nao pertencerem ao alfabeto e ao alfabeto de caracteres especiais

        return password.filterNot {
            // pega todos que nao sao especiais
            ALLOWED_SPECIAL_CHARACTERS.contains(it)
        }.filterNot {
            // pega todos que nao sao especiais nem letra minuscula
            LOWER_CASE_LETTERS.contains(it)
        }.filterNot {
            // pega todos que nao sao especiais nem letra minuscula nem maiuscula
            UPPER_CASE_LETTERS.contains(it)
        }.filterNot {
            // e que nao sao numeros
            it.isDigit()

            // se estiver vazio, eh valido pois nao tem nenhuma letra estranha
        }.isEmpty()
    }
}