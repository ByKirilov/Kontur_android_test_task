package com.bykirilov.kontur_android_test_task.model

import com.bykirilov.kontur_android_test_task.network.APIService

class ContactsRemoteDataSource(val apiService: APIService) {

    suspend fun getContacts(): List<Contact> {
        val result = mutableListOf<Contact>()
        for (i in 1..3) {
            result += apiService.getContacts(i)
        }
        return result.toList()
    }
}