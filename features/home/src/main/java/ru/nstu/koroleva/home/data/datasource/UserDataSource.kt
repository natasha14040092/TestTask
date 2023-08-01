package ru.nstu.koroleva.home.data.datasource

import ru.nstu.koroleva.home.domain.entity.UserEntity

interface UserDataSource {
    fun getUser() : UserEntity

    fun clearUserInfo()
}