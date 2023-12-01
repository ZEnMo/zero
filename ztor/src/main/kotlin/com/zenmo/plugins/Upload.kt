package com.zenmo.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.azure.storage.blob.BlobServiceClientBuilder
import com.azure.storage.blob.sas.BlobSasPermission
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues
import com.azure.storage.common.sas.SasProtocol
import com.zenmo.companysurvey.BlobPurpose
import com.zenmo.companysurvey.BlobStorage
import com.zenmo.errorMessageToJson
import io.ktor.http.*
import io.ktor.server.plugins.*
import java.time.OffsetDateTime

fun Application.configureUpload() {
    val blobStorage = BlobStorage()

    routing {
        // Get a shared access signature (SAS) token that can be used to upload a blob.
        get("/upload-url") {
            val queryParams = call.request.queryParameters

            val required = listOf("project", "company", "fileName", "purpose")
            val missing = required.filter { !queryParams.contains(it) }
            if (missing.isNotEmpty()) {
                call.respond(HttpStatusCode.BadRequest, errorMessageToJson("Missing query parameters: ${missing.joinToString(", ")}"))
                return@get
            }

            val url = blobStorage.getBlobSasUrl(
                BlobPurpose.valueOf(queryParams["purpose"]!!),
                queryParams["project"]!!,
                queryParams["company"]!!,
                queryParams["fileName"]!!,
            )

            call.respond(mapOf("uploadUrl" to url));
        }
    }
}