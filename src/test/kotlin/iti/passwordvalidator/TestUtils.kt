package iti.passwordvalidator


data class TestArgument(
        val password: String,
        val expected: Boolean,
        val testMessage: String
)