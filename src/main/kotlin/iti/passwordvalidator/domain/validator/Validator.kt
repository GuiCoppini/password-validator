package iti.passwordvalidator.domain.validator

import iti.passwordvalidator.domain.model.Constraint


// utilizar uma interface e diversas implementacoes facilita o teste unitario de cada logica de negocio
interface Validator {

    // isso esta aqui pois, como interface de validacao, o alfabeto de alguns caracteres pode estar aqui
    // uma vez que isso permite que as classes filhas possam utilizar
    // (como as que validam char especial e char invalido)

    // tambem permite que o alfabeto valido seja facilmente modificado
    object Alphabets {
        internal val ALLOWED_SPECIAL_CHARACTERS = listOf('!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+')
        internal val UPPER_CASE_LETTERS = 'A' .. 'Z'
        internal val LOWER_CASE_LETTERS = 'a' .. 'z'
    }
    val constraint: Constraint

    fun validate(password: String): Boolean
}
