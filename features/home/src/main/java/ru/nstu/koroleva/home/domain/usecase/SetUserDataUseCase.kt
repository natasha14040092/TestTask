package ru.nstu.koroleva.home.domain.usecase

import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.home.domain.repository.UserDataRepository

class SetUserDataUseCase(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(user: UserEntity) = userDataRepository.setUserData(user)
}