package ru.nstu.koroleva.n.login.domain.usecase

import ru.nstu.koroleva.n.login.domain.entity.UserEntity
import ru.nstu.koroleva.n.login.domain.repository.UserDataRepository

class SetUserDataUseCase(
    private val userDataRepository: UserDataRepository
) {
    operator fun invoke(user: UserEntity) {
        userDataRepository.setUserData(user)
    }
}