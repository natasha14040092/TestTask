package ru.nstu.koroleva.n.login.presentation

sealed class SignUpErrorState {
    object NoError : SignUpErrorState()
    object ShortTextError : SignUpErrorState()
    object SpecialSymbolError : SignUpErrorState()
    object DigitError : SignUpErrorState()
    object UppercaseError : SignUpErrorState()
    object PasswordMatchError : SignUpErrorState()
}

