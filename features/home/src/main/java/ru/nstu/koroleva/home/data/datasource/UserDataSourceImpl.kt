package ru.nstu.koroleva.home.data.datasource

import ru.nstu.koroleva.home.data.mapper.toEntity
import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider

class UserDataSourceImpl(
    private val userSharedPreferencesProvider: UserSharedPreferencesProvider
) : UserDataSource {
    override fun getUser(): UserEntity =
        userSharedPreferencesProvider.getUserInfo().toEntity()
}