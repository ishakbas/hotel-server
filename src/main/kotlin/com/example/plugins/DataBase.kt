package com.example.plugins

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5433/hotel_test2?characterEncoding=utf8&useUnicode=true",
        user = "testLogin",
        driver = "org.postgresql.Driver",
        password = "test"
    )
}
