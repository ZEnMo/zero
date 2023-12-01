package com.zenmo

fun errorMessageToJson(message: String?): Any {
    return mapOf(
        "error" to mapOf(
            "message" to message
        )
    )
}