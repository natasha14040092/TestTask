package ru.nstu.koroleva.home.presentation

sealed class HomeState {
    data class Content(
        val userName: String
    ) : HomeState()

    data class Dialog(
        val nameText: String,
        val surnameText: String,
        val birthdateText: String,
        val passwordText: String,
        val nameError: DataErrorState,
        val surnameError: DataErrorState,
        val birthdateError: DataErrorState
    ) : HomeState()
}
