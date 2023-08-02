package ru.nstu.koroleva.home.domain.repository

import ru.nstu.koroleva.home.domain.entity.UserEntity

interface UserDataRepository {
    fun getUserData() : UserEntity

    fun clearUserData()

    fun setUserData(userEntity: UserEntity)
}