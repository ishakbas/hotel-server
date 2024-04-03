package com.example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

@Serializable
data class ExposedHotelRooms(
    val name: String,
    val roomTypeId: Int,
    @SerialName("room_image")
    val roomImage: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExposedHotelRooms

        if (name != other.name) return false
        if (roomTypeId != other.roomTypeId) return false
        if (roomImage != null) {
            if (other.roomImage == null) return false
            if (!roomImage.contentEquals(other.roomImage)) return false
        } else if (other.roomImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + roomTypeId
        result = 31 * result + (roomImage?.contentHashCode() ?: 0)
        return result
    }
}

object HotelRooms : IntIdTable("hotel_rooms") {
    val name = varchar("name", 50)
    val room_type_id = integer("room_type_id").references(RoomTypes.id)
    val room_image = binary("room_image").nullable()
}