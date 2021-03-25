package com.bykirilov.kontur_android_test_task.model

import com.bykirilov.kontur_android_test_task.managers.NetManager

class ContactsRepository(val netManager: NetManager) {

    private val localDataSource = ContactsLocalDataSource()
    private val remoteDataSource = ContactsRemoteDataSource()

    fun getContacts(): List<Contact>? {
        netManager.isConnectedToInternet?.let {
            return if (it) {
                remoteDataSource.getContacts().also { contacts ->
                    localDataSource.saveContacts(contacts)
                }
            } else {
                null
            }
        }

        return localDataSource.getContacts()
    }
}