package ru.nstu.koroleva.home.data.mapper

import ru.nstu.koroleva.home.domain.entity.UserEntity
import ru.nstu.koroleva.n.preferences.model.UserModel

fun UserModel.toEntity(): UserEntity =
    UserEntity(this.name, this.surname, this.birthdate, this.password)