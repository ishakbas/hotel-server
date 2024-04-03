package com.example.data

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class ExposedRoomTypes(
    val name: String,
    val description: String?
)

object RoomTypes : IntIdTable("room_types") {
    val name = varchar("name", 50)
    val description = text("description").nullable()
}