package com.bykirilov.kontur_android_test_task.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_table")
data class Contact(
    @PrimaryKey
    @ColumnInfo(name="id")
    val id: String,

    @ColumnInfo(name="name")
    val name: String,

    @ColumnInfo(name="phone")
    val phone: String,

    @ColumnInfo(name="height")
    val height: Float,

    @ColumnInfo(name="biography")
    val biography: String,

    @ColumnInfo(name="temperament")
    val temperament: String,

    @ColumnInfo(name="educationStart")
    val educationStart: String,

    @ColumnInfo(name="educationEnd")
    val educationEnd: String,
)