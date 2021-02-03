package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint
import org.springframework.stereotype.Service

@Service
class RepeatingCharacterValidator : Validator {

    // facilita a alteracao da regra
    private val ALLOWED_APPEARENCES = 1
    override val constraint: Constraint
        get() = Constraint.NO_REPEATING_CHARACTER


    override fun validate(password: String): Boolean =
            // agrupa por cada char
            password.groupingBy { it }
                    // faz um map<char, aparicoes (int)>
                    .eachCount()
                    // verifica se algum aparece mais do que o quanto pode
                    .all { it.value <= ALLOWED_APPEARENCES }
}