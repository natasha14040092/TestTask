package ru.nstu.koroleva.n.login.domain.entity

data class UserEntity(
    val name: String,
    val surname: String,
    val birthdate: String,
    val password: String
)
