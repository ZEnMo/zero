package com.zenmo.plugins

import com.zenmo.companysurvey.SurveyRepository
import com.zenmo.companysurvey.dto.*
import com.zenmo.energieprestatieonline.RawPandTable
import com.zenmo.companysurvey.table.CompanySurveyGridConnectionTable
import com.zenmo.companysurvey.table.CompanySurveyTable
import com.zenmo.dbutil.createEnumTypeSql
import com.zenmo.errorMessageToJson
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.*
import kotlinx.coroutines.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.configureDatabases(): Database {
    val db: Database = connectToPostgres(embedded = false)

//    createSchema(db)

    routing {
        // Create
        post("/company-survey") {
            var survey: Survey? = null
            try {
                survey = call.receive<Survey>()
            } catch (e: BadRequestException) {
                if (e.cause is JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest, errorMessageToJson(e.cause?.message))
                    return@post
                }
                call.respond(HttpStatusCode.BadRequest,  errorMessageToJson(e.message))
                return@post
            }

            val repository = SurveyRepository(db)
            repository.save(survey)

            // TODO: return entity from database
            call.respond(HttpStatusCode.Created, survey)
        }
    }

    return db
}

fun createSchema(db: Database) {
    transaction(db) {
        exec(createEnumTypeSql(KleinverbruikElectricityConnectionCapacity::class.java))
        exec(createEnumTypeSql(KleinverbruikElectricityConsumptionProfile::class.java))
        exec(createEnumTypeSql(HeatingType::class.java))
        exec(createEnumTypeSql(PVOrientation::class.java))

        SchemaUtils.create(CompanySurveyTable, CompanySurveyGridConnectionTable, RawPandTable)
    }
}

/**
 * @param embedded -- if [true] defaults to an embedded database for tests that runs locally in the same process.
 * In this case you don't have to provide any parameters in configuration file, and you don't have to run a process.
 *
 * @return [Connection] that represent connection to the database. Please, don't forget to close this connection when
 * your application shuts down by calling [Connection.close]
 * */
fun connectToPostgres(embedded: Boolean): Database {
    Class.forName("org.postgresql.Driver")
    return if (embedded) {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver", user = "root", password = "")
    } else {
        val url = System.getenv("POSTGRES_URL")
        val user = System.getenv("POSTGRES_USER")
        val password = System.getenv("POSTGRES_PASSWORD")

        Database.connect(url, driver = "org.postgresql.Driver", user, password)
    }
}
