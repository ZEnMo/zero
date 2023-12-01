package com.zenmo

import com.zenmo.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    if (args.count() == 1) {
        if (args[0] == "create-schema") {
            println("Creating database schema...")
            val db = connectToPostgres(false)
            createSchema(db)
            println("Schema created!")
            return
        }

        println("Unknown argument: ${args[0]}")
    }

    embeddedServer(Netty, port = 8082, host = "0.0.0.0", module = Application::module)
            .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureStatusPages()
    configureUpload()
}
