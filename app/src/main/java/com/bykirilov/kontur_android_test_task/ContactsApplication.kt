package com.bykirilov.kontur_android_test_task

import android.app.Application
import com.bykirilov.kontur_android_test_task.model.ContactsRepository
import com.bykirilov.kontur_android_test_task.model.database.ContactRoomDatabase

class ContactsApplication : Application() {
    val database by lazy { ContactRoomDatabase.getDatabase(this) }
    val repository by lazy { ContactsRepository(this, database.contactDao()) }
}