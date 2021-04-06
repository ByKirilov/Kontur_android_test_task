package com.bykirilov.kontur_android_test_task.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.*
import com.bykirilov.kontur_android_test_task.model.ContactsRepository
import com.bykirilov.kontur_android_test_task.model.database.Contact
import kotlinx.coroutines.launch

class ContactDetailsViewModel(private val repository: ContactsRepository) : ViewModel() {
    val contact = MutableLiveData<Contact?>()

    fun loadContactById(id: String) = viewModelScope.launch {
        contact.value = repository.getContactById(id)
    }

    fun dialPhoneNumber(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            startActivity(context, intent, null)
        }
    }
}

class ContactDetailsViewModelFactory(private val repository: ContactsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}