package ru.nstu.koroleva.n.login.data.mapper

import ru.nstu.koroleva.n.login.domain.entity.UserEntity
import ru.nstu.koroleva.n.preferences.model.UserModel

fun UserEntity.toModel(): UserModel =
    UserModel(this.name, this.surname, this.birthdate, this.password)