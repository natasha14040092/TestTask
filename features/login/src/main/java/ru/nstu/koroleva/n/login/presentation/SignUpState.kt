package ru.nstu.koroleva.n.login.presentation

sealed class SignUpState {
    //текущие состояние экрана
    data class Content(
        val loginText: String,
        val passwordText: String,
        val repeatPasswordText: String,
        val loginError: SignUpLoginErrorState,
        val passwordError: SignUpPasswordErrorState,
        val repeatPasswordError: SignUpRepeatPasswordErrorState
    ) : SignUpState()

    object Ok : SignUpState()
}
