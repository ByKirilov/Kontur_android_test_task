package com.bykirilov.kontur_android_test_task.model

import androidx.annotation.WorkerThread
import com.bykirilov.kontur_android_test_task.model.database.Contact
import com.bykirilov.kontur_android_test_task.model.database.ContactDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ContactsLocalDataSource(private val contactDao: ContactDao) {

    val contacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun saveContacts(contacts: List<Contact>) {
        for (contact in contacts) {
            contactDao.insert(contact)
        }
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    suspend fun deleteAll() {
        contactDao.deleteAll()
    }
}