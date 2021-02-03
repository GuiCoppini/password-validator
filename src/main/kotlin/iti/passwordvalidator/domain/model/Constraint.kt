package iti.passwordvalidator.domain.model


enum class Constraint(val message: String) {
    MINIMUM_SIZE("Password must contain at least 9 characters"),
    DIGITS_COUNT("Password must contain at least 1 number"),
    LOWER_CASE_LETTER_COUNT("Password must contain at least 1 lower case letter"),
    UPPER_CASE_LETTER_COUNT("Password must contain at least 1 upper case letter"),
    SPECIAL_CHARACTER("Password must contain at least 1 special character"),
    INVALID_CHARACTER("Password must not contain invalid characters"),
    NO_REPEATING_CHARACTER("Password must not contain any repeating characters")
}