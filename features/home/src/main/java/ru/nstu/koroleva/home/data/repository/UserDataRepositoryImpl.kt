package ru.nstu.koroleva.home.data.repository

import ru.nstu.koroleva.home.data.datasource.UserDataSource
import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.home.domain.repository.UserDataRepository

class UserDataRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserDataRepository {
    override fun getUserData(): UserEntity = userDataSource.getUserData()

    override fun clearUserData() = userDataSource.clearUserData()

    override fun setUserData(userEntity: UserEntity) = userDataSource.setUserData(userEntity)
}