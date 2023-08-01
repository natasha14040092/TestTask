package ru.nstu.koroleva.home.domain.usecase

import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.home.domain.repository.UserDataRepository

class GetUserDataUseCase(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke() : UserEntity = userDataRepository.getUserData()
}