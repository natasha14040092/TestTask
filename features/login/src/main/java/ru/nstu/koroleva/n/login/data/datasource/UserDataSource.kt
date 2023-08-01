package ru.nstu.koroleva.n.login.data.datasource

import ru.nstu.koroleva.n.login.domain.entity.UserEntity

interface UserDataSource {
    fun setUser(user: UserEntity)
}