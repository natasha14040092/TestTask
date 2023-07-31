package ru.nstu.koroleva.n.login.data.mapper

import androidx.collection.arraySetOf
import ru.nstu.koroleva.n.login.domain.entity.UserEntity

fun UserEntity.toSet(): Set<String> =
    arraySetOf(this.name, this.surname, this.birthdate, this.password)