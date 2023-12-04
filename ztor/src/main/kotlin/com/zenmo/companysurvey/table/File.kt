package com.zenmo.companysurvey.table

import com.zenmo.blob.BlobPurpose
import org.jetbrains.exposed.sql.Table

object FileTable: Table("file") {
    val gridConnectionId = uuid("grid_connection_id").references(CompanySurveyGridConnectionTable.id)
    val purpose = enumerationByName("purpose", 100, BlobPurpose::class)

    val remoteName = varchar("remote_name", 1000)
    val originalName = varchar("original_name", 100)
    val contentType = varchar("content_type", 100)
    val size = integer("size")

    override val primaryKey = PrimaryKey(remoteName)
}
