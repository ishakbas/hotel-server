package com.example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class ExposedRoomTypes(
    val name: String,
    val description: String?,
    @SerialName("bed_count")
    val bedCount: Int?,
    val wifi: Boolean?,
    val kitchen: Boolean?,
    val tv: Boolean?
)

object RoomTypes : IntIdTable("room_types") {
    val name = varchar("name", 50)
    val description = text("description").nullable()
    val bed_count = integer("bed_count").nullable()
    val wifi = bool("wifi").nullable()
    val kitchen = bool("kitchen").nullable()
    val tv = bool("tv").nullable()
}

