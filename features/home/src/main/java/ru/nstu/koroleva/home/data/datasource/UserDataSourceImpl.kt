package ru.nstu.koroleva.home.data.datasource

import ru.nstu.koroleva.home.data.mapper.toEntity
import ru.nstu.koroleva.home.data.mapper.toModel
import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.n.preferences.UserSharedPreferencesProvider

class UserDataSourceImpl(
    private val userSharedPreferencesProvider: UserSharedPreferencesProvider
) : UserDataSource {
    override fun getUserData(): UserEntity =
        userSharedPreferencesProvider.getUserInfo().toEntity()

    override fun clearUserData() =
        userSharedPreferencesProvider.clearUserInfo()

    override fun setUserData(userEntity: UserEntity) {
        userSharedPreferencesProvider.putUserInfo(userEntity.toModel())
    }
}