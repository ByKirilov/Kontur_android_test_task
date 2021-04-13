package com.bykirilov.kontur_android_test_task.viewModel

import androidx.lifecycle.*
import com.bykirilov.kontur_android_test_task.Event
import com.bykirilov.kontur_android_test_task.model.ContactsRepository
import com.bykirilov.kontur_android_test_task.model.database.Contact
import kotlinx.coroutines.*

class ContactsListViewModel(private val repository: ContactsRepository) : ViewModel() {

    val contacts: LiveData<List<Contact>> = repository.contacts.asLiveData()

    private val _showSnackBar = MutableLiveData<Event<String>>()
    val showSnackBar: LiveData<Event<String>>
        get() = _showSnackBar

    val isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun preferContacts() {
        if (repository.isDataObsolete) {
            updateContacts()
        }
    }

    fun updateContacts() = viewModelScope.launch {
        isLoading.value = true
        repository.clearContacts()
        loadContacts()
        isLoading.value = false
    }

    private suspend fun loadContacts() {
        val requestCode = repository.loadContactsWithRequestCode()
        if (requestCode == ContactsRepository.NO_INTERNET_REQUEST_CODE) {
            _showSnackBar.value = Event(NO_INTERNET_MESSAGE)
        }
    }

    companion object {
        private const val NO_INTERNET_MESSAGE = "Нет подключения к сети"
    }
}

class ContactsListViewModelFactory(private val repository: ContactsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}