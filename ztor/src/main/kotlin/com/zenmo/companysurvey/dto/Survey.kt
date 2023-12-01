package com.zenmo.companysurvey.dto

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Root object
 */
@Serializable
data class Survey(
    val created: Instant = Clock.System.now(),
    val project: String,
    val companyName: String,
    val personName: String,
    val email: String = "",

    val transport: Transport,
    val gridConnections: List<SurveyGridConnection>,
)

