package ru.nstu.koroleva.home.presentation

import ru.nstu.koroleva.home.domain.entity.UserEntity

sealed class HomeState {
    data class Content(
        val userName: String
    ) : HomeState()

    data class Dialog(
        val userEntity: UserEntity
    ) : HomeState()
}
