package com.bykirilov.kontur_android_test_task.model

import com.bykirilov.kontur_android_test_task.model.database.Contact
import com.bykirilov.kontur_android_test_task.network.APIService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ContactsRemoteDataSource(val apiService: APIService) {

    suspend fun getContacts(): List<Contact> {
        val result = mutableListOf<ContactPOJO>()
        for (i in 1..3) {
            val contact = apiService.getContacts(i)
            result += contact
        }
        return result.map { it.toContact() }
    }
}