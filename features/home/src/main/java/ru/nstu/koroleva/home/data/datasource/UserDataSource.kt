package ru.nstu.koroleva.home.data.datasource

import ru.nstu.koroleva.home.domain.entity.UserEntity

interface UserDataSource {
    fun getUserData() : UserEntity

    fun clearUserData()

    fun setUserData(userEntity: UserEntity)
}