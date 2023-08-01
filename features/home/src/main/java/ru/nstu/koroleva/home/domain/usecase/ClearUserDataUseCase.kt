package ru.nstu.koroleva.home.domain.usecase

import ru.nstu.koroleva.home.domain.repository.UserDataRepository

class ClearUserDataUseCase(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke() = userDataRepository.clearUserData()
}