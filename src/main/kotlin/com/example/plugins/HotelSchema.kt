package com.example.plugins

import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

class HotelService {

    class UserRepository {

        suspend fun login(user: ExposedUsers): Boolean = dbQuery {
            Users.select { Users.login eq user.login and (Users.password eq user.password) }.count() > 0
        }

        suspend fun getUser(user: ExposedUsers): ExposedUserInfo? = dbQuery {
            Users.select { Users.login.eq(user.login) and Users.password.eq(user.password) }.map {
                it[Users.admin]?.let { it1 ->
                    ExposedUserInfo(
                        it[Users.id].value,
                        it[Users.login],
                        it[Users.password],
                        it1
                    )
                }
            }.singleOrNull()
        }

        suspend fun read(id: Int): ExposedUsers? {
            return dbQuery {
                Users.select { Users.id eq id }.map {
                    it[Users.admin]?.let { it1 ->
                        ExposedUsers(
                            it[Users.login],
                            it[Users.password],
                            it1
                        )
                    }
                }.singleOrNull()
            }
        }

        suspend fun create(user: ExposedUsers): Int = dbQuery {
            Users.insert {
                it[login] = user.login
                it[password] = user.password
                it[admin] = user.admin
            }[Users.id].value
        }

        suspend fun update(id: Int, user: ExposedUsers) {
            dbQuery {
                Users.update({ Users.id eq id }) {
                    it[login] = user.login
                    it[password] = user.password
                    it[admin] = user.admin
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                Users.deleteWhere { Users.id.eq(id) }
            }
        }
    }

    class BookingsRepository {
        suspend fun read(id: Int): ExposedBookings? {
            return dbQuery {
                Bookings.select { Bookings.id eq id }.map {
                    ExposedBookings(
                        it[Bookings.user_id],
                        it[Bookings.room_id],
                        it[Bookings.check_in_date],
                        it[Bookings.check_out_date],
                        it[Bookings.status]
                    )
                }.singleOrNull()
            }
        }

        suspend fun create(booking: ExposedBookings): Int = dbQuery {
            Bookings.insert {
                it[user_id] = booking.user_id
                it[room_id] = booking.room_id
                it[check_in_date] = booking.check_in_date
                it[check_out_date] = booking.check_out_date
                it[status] = booking.status
            }[Bookings.id].value
        }

        suspend fun update(id: Int, bookings: ExposedBookings) {
            dbQuery {
                Bookings.update({ Bookings.id eq id }) {
                    it[user_id] = bookings.user_id
                    it[room_id] = bookings.room_id
                    it[check_in_date] = bookings.check_in_date
                    it[check_out_date] = bookings.check_out_date
                    it[status] = bookings.status
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                Bookings.deleteWhere { Bookings.id eq id }
            }
        }
    }

    class HotelRoomsRepository {
        suspend fun read(id: Int): ExposedHotelRoomsWithTypes? {
            return dbQuery {
                HotelRooms.innerJoin(RoomTypes).select { HotelRooms.id eq id }.map {
                    ExposedHotelRoomsWithTypes(
                        it[HotelRooms.id].value, it[HotelRooms.name], it[RoomTypes.name], it[HotelRooms.room_image]
                    )
                }.singleOrNull()
            }
        }

        suspend fun readAllRooms(): List<ExposedHotelRoomsWithTypes> {
            return dbQuery {
                (HotelRooms innerJoin RoomTypes).selectAll().map {
                    ExposedHotelRoomsWithTypes(
                        it[HotelRooms.id].value, it[HotelRooms.name], it[RoomTypes.name], it[HotelRooms.room_image]
                    )
                }
            }
        }

        suspend fun create(hotelRooms: ExposedHotelRooms) = dbQuery {
            HotelRooms.insert {
                it[name] = hotelRooms.name
                it[room_type_id] = hotelRooms.roomTypeId
                it[room_image] = hotelRooms.room_image
            }
        }

        suspend fun update(id: Int, hotelRooms: ExposedHotelRooms) {
            dbQuery {
                HotelRooms.update({ HotelRooms.id eq id }) {
                    it[name] = hotelRooms.name
                    it[room_type_id] = hotelRooms.roomTypeId
                    it[room_image] = hotelRooms.room_image
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                HotelRooms.deleteWhere { HotelRooms.id.eq(id) }
            }
        }
    }

    class RoomTypesRepository {
        suspend fun create(roomType: ExposedRoomTypes): EntityID<Int> = dbQuery {
            RoomTypes.insert {
                it[name] = roomType.name
                it[description] = roomType.description
            }[RoomTypes.id]
        }

        suspend fun read(id: Int): ExposedRoomTypes? {
            return dbQuery {
                RoomTypes.select { RoomTypes.id eq id }
                    .map { ExposedRoomTypes(it[RoomTypes.name], it[RoomTypes.description]) }.singleOrNull()
            }
        }

        suspend fun update(id: Int, roomType: ExposedRoomTypes) {
            dbQuery {
                RoomTypes.update({ RoomTypes.id eq id }) {
                    it[name] = roomType.name
                    it[description] = roomType.description
                }
            }
        }

        suspend fun delete(id: Int) {
            dbQuery {
                RoomTypes.deleteWhere { RoomTypes.id.eq(id) }
            }
        }
    }

}


@Serializable
data class ExposedHotelRoomsWithTypes(
    val id: Int,
    val name: String,
    val roomType: String,
    val room_image: ByteArray?
)