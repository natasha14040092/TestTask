package ru.nstu.koroleva.n.login.presentation

//поискать инфу о наследовании sealed class
sealed class SignUpLoginErrorState {
    object NoError : SignUpLoginErrorState()
    object EmptyTextError : SignUpLoginErrorState()
    object ShortTextError : SignUpLoginErrorState()
    object SpecialSymbolError : SignUpLoginErrorState()
}

sealed class SignUpPasswordErrorState {
    object NoError : SignUpPasswordErrorState()
    object EmptyTextError : SignUpPasswordErrorState()
    object ShortTextError : SignUpPasswordErrorState()
}

sealed class SignUpRepeatPasswordErrorState {
    object NoError : SignUpRepeatPasswordErrorState()
    object EmptyTextError : SignUpRepeatPasswordErrorState()
    object PasswordMatchError : SignUpRepeatPasswordErrorState()
}


