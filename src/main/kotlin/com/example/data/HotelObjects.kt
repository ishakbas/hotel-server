package com.example.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

@Serializable
data class ExposedBookings(
    val user_id: Int,
    val room_id: Int,
    val check_in_date: LocalDate,
    val check_out_date: LocalDate,
    val status: String?
)

object Bookings : IntIdTable("bookings") {
    val user_id = integer("user_id")
    val room_id = integer("room_id")
    val check_in_date = date("check_in_date")
    val check_out_date = date("check_out_date")
    val status = varchar("status", 20).nullable()
}

@Serializable
data class ExposedRoomTypes(
    val name: String,
    val description: String?
)

object RoomTypes : IntIdTable("room_types") {
    val name = varchar("name", 50)
    val description = text("description").nullable()
}

@Serializable
data class ExposedHotelRooms(
    val name: String,
    val roomTypeId: Int,
    val room_image: ByteArray?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ExposedHotelRooms

        if (name != other.name) return false
        if (roomTypeId != other.roomTypeId) return false
        if (room_image != null) {
            if (other.room_image == null) return false
            if (!room_image.contentEquals(other.room_image)) return false
        } else if (other.room_image != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + roomTypeId
        result = 31 * result + (room_image?.contentHashCode() ?: 0)
        return result
    }
}

object HotelRooms : IntIdTable("hotel_rooms") {
    val name = varchar("name", 50)
    val room_type_id = integer("room_type_id").references(RoomTypes.id)
    val room_image = binary("room_image").nullable()
}

@Serializable
data class ExposedUsers(
    val login: String,
    val password: String,
    val admin: Boolean
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
