package ru.nstu.koroleva.n.login.presentation

sealed class SignUpState {
    //текущее состояние экрана
    data class Content(
        val nameText: String,
        val surnameText: String,
        val birthdateText: String,
        val passwordText: String,
        val repeatPasswordText: String,
        val signUpButtonClick: Boolean,
        val nameError: SignUpErrorState,
        val surnameError: SignUpErrorState,
        val birthdateError: SignUpErrorState,
        val passwordError: SignUpErrorState,
        val repeatPasswordError: SignUpErrorState
    ) : SignUpState()

    object Ok : SignUpState()
}
