package com.example.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

@Serializable
data class ExposedBookings(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("room_id")
    val roomId: Int,
    @SerialName("check_in_date")
    val checkInDate: LocalDate,
    @SerialName("check_out_date")
    val checkOutDate: LocalDate,
    val status: String?
)

object Bookings : IntIdTable("bookings") {
    val user_id = integer("user_id")
    val room_id = integer("room_id")
    val check_in_date = date("check_in_date")
    val check_out_date = date("check_out_date")
    val status = varchar("status", 20).nullable()
}