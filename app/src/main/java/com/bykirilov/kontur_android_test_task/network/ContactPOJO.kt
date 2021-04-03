package com.bykirilov.kontur_android_test_task.model

import com.bykirilov.kontur_android_test_task.model.database.Contact
import com.google.gson.annotations.SerializedName

data class ContactPOJO(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("height")
        val height: Float,
        @SerializedName("biography")
        val biography: String,
        @SerializedName("temperament")
        val temperament: String,
        @SerializedName("educationPeriod")
        val educationPeriod: EducationPeriod
)

data class EducationPeriod(
        @SerializedName("start")
        val start: String,
        @SerializedName("end")
        val end: String,
)

fun ContactPOJO.toContact(): Contact {
        return Contact(
                id,
                name,
                phone,
                height,
                biography,
                temperament,
                educationPeriod.start,
                educationPeriod.end
        )
}
