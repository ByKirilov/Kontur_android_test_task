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

    private val localDataSource = ContactsLocalDataSource(contactDao)
    private val remoteDataSource = ContactsRemoteDataSource(APIService.create())

    val contacts = localDataSource.contacts

    val isDataObsolete: Boolean
        get() {
            val lastDownloadTime = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                    .getLong(LAST_DOWNLOAD_TIME_KEY, 0L)
            val currentTime = System.currentTimeMillis()
            return currentTime - lastDownloadTime > TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
        }

    suspend fun loadContactsWithRequestCode() : Int {
        val netManager = NetManager(context)
        netManager.isConnectedToInternet?.let {
             if (it) {
                 return try {
                     remoteDataSource.getContacts().also { contacts ->
                         localDataSource.saveContacts(contacts)
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

    suspend fun insert(contact: Contact) = localDataSource.insert(contact)

    suspend fun clearContacts() = localDataSource.deleteAll()

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