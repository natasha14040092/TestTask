package ru.nstu.koroleva.n.login.data.repository

import ru.nstu.koroleva.n.login.data.datasource.UserDataSource
import ru.nstu.koroleva.n.login.domain.entity.UserEntity
import ru.nstu.koroleva.n.login.domain.repository.UserDataRepository

class UserDataRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserDataRepository {
    override fun setUserData(user: UserEntity) {
        userDataSource.setUser(user)
    }
}