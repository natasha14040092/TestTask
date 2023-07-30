package ru.nstu.koroleva.n.login.domain.repository

import ru.nstu.koroleva.n.login.domain.entity.UserEntity

interface UserDataRepository {
    fun setUserData(user: UserEntity)
}