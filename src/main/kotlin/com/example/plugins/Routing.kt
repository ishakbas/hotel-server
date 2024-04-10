package com.example.plugins

import com.example.data.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        users()
        rooms()
        roomTypes()
        bookings()
    }
}

const val invalidId = "Invalid ID"
fun Route.users() {
    val userService = HotelService.UserRepository()
    route("/user/") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val user = userService.read(id)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val user = call.receive<ExposedUsers>()
                userService.update(id, user)
            }

            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                userService.delete(id)
                call.respond(HttpStatusCode.OK, "user $id was deleted")
            }
        }
        route("login") {
            post {
                try {
                    val user = call.receive<ExposedUsers>()
                    val wasFound = userService.login(user)
                    if (wasFound) {
                        val foundUser: ExposedUserInfo = userService.getUser(user)!!
                        call.respond(HttpStatusCode.Found, foundUser)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User wasn't found")
                    }
                } catch (ex: Exception) {
                    println(ex.message)
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
        }
        route("register") {
            post {
                val user = call.receive<ExposedUsers>()
                if (userService.login(user)) {
                    call.respond(HttpStatusCode.Conflict, "user already exist")
                } else {
                    try {
                        val id = userService.create(user)
                        call.respond(HttpStatusCode.Created, id)
                    } catch (ex: Exception) {
                        call.respond(HttpStatusCode.InternalServerError, ex.message.toString())
                    }
                }
            }
        }
    }
}

fun Route.rooms() {
    val roomService = HotelService.HotelRoomsRepository()
    route("/room/") {
        post {
            val room = call.receive<ExposedHotelRooms>()
            roomService.create(room)
            call.respond(HttpStatusCode.Created)
        }
        route("all") {
            get {
                val rooms = roomService.readAllRooms()
                call.respond(HttpStatusCode.OK, rooms)
            }
        }
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val room = roomService.read(id)
                if (room != null) {
                    call.respond(HttpStatusCode.OK, room)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val room = call.receive<ExposedHotelRooms>()
                roomService.update(id, room)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                roomService.delete(id)
            }
        }
    }
}

fun Route.roomTypes() {
    val roomTypesService = HotelService.RoomTypesRepository()
    route("/room_types") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val roomType = roomTypesService.read(id)
                if (roomType != null) {
                    call.respond(HttpStatusCode.OK, roomType)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val roomType = call.receive<ExposedRoomTypes>()
                roomTypesService.update(id, roomType)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                roomTypesService.delete(id)
            }
        }
        post {
            val roomType = call.receive<ExposedRoomTypes>()
            val id = roomTypesService.create(roomType)
            call.respond(HttpStatusCode.Created, id)
        }
    }
}

fun Routing.bookings() {
    val bookingService = HotelService.BookingsRepository()
    route("/booking/") {
        route("{id}") {
            get {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val booking = bookingService.read(id)
                if (booking != null) {
                    call.respond(HttpStatusCode.OK, booking)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
            put {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                val booking = call.receive<ExposedBookings>()
                bookingService.update(id, booking)
            }
            delete {
                val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException(invalidId)
                bookingService.delete(id)
                call.respond(HttpStatusCode.OK)
            }
        }
        post {
            try {
                val booking = call.receive<ExposedBookings>()
                val id = bookingService.create(booking)
                call.respond(HttpStatusCode.Created, id)
            } catch (ex: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ex.message.toString())
            }
        }
    }
}
