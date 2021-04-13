package com.bykirilov.kontur_android_test_task.model

import android.content.Context
import com.bykirilov.kontur_android_test_task.managers.NetManager
import com.bykirilov.kontur_android_test_task.model.database.Contact
import com.bykirilov.kontur_android_test_task.model.database.ContactDao
import com.bykirilov.kontur_android_test_task.network.APIService
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class ContactsRepository(private val context: Context, private val contactDao: ContactDao) {

    val contacts = contactDao.getAllContacts()

    val isDataObsolete: Boolean
        get() {
            val lastDownloadTime = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .getLong(LAST_DOWNLOAD_TIME_KEY, 0L)
            val currentTime = System.currentTimeMillis()
            return currentTime - lastDownloadTime > TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
        }

    suspend fun clearContacts() = contactDao.deleteAll()

    suspend fun getContactById(id: String) = contactDao.getContactById(id)

    suspend fun loadContactsWithRequestCode() : Int {
        val netManager = NetManager(context)
        netManager.isConnectedToInternet?.let {
             if (it) {
                 return try {
                     getContacts().also { contacts ->
                         saveContacts(contacts)
                     }
                     updateLastDownloadTime()
                     SUCCESS_REQUEST_CODE
                 } catch (e: ConnectException) {
                     NO_INTERNET_REQUEST_CODE
                 } catch (e: UnknownHostException) {
                     NO_INTERNET_REQUEST_CODE
                 }
            }
        }
        return NO_INTERNET_REQUEST_CODE
    }

    private suspend fun getContacts(): List<Contact> {
        val result = mutableListOf<ContactPOJO>()
        val apiService = APIService.create()
        for (i in 1..3) {
            val contact = apiService.getContacts(i)
            result += contact
        }
        return result.map { it.toContact() }
    }

    private suspend fun saveContacts(contacts: List<Contact>) {
        for (contact in contacts) {
            contactDao.insert(contact)
        }
    }

    private fun updateLastDownloadTime() {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putLong(LAST_DOWNLOAD_TIME_KEY, System.currentTimeMillis()).apply()
    }

    companion object {
        const val SUCCESS_REQUEST_CODE = 0
        const val NO_INTERNET_REQUEST_CODE = 1

        const val LAST_DOWNLOAD_TIME_KEY = "lastDownloadTime"
    }
}