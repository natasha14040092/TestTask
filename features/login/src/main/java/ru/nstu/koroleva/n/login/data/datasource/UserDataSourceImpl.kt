package ru.nstu.koroleva.n.login.data.datasource

import ru.nstu.koroleva.n.login.data.mapper.toModel
import ru.nstu.koroleva.n.login.domain.entity.UserEntity
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider

class UserDataSourceImpl(
    private val userSharedPreferencesProvider: UserSharedPreferencesProvider
) : UserDataSource {

    override fun setUser(user: UserEntity) = userSharedPreferencesProvider.putUserInfo(user.toModel())
}