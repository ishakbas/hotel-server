package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable


@Serializable
data class ExposedUsers(
    val login: String,
    val password: String,
    val admin: Boolean = false
)

@Serializable
data class ExposedUserInfo(
    val id: Int,
    val login: String,
    val password: String,
    val admin: Boolean
)

object Users : IntIdTable("users") {
    val login = varchar("login", 50).uniqueIndex()
    val password = varchar("password", 50)
    val admin = bool("admin").nullable()
}
