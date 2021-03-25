package com.bykirilov.kontur_android_test_task.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bykirilov.kontur_android_test_task.managers.NetManager
import com.bykirilov.kontur_android_test_task.model.Contact
import com.bykirilov.kontur_android_test_task.model.ContactsRepository
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class ContactsListViewModel(application: Application): AndroidViewModel(application), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw e }

    private val contactsRepository = ContactsRepository(NetManager(getApplication()))

    fun getContacts() = launch {
        val contacts = contactsRepository.getContacts()
    }
}