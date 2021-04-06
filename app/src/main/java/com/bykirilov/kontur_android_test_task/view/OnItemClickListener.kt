package com.bykirilov.kontur_android_test_task.view

import com.bykirilov.kontur_android_test_task.model.database.Contact

interface OnItemClickListener {
    fun onItemClicked(contact: Contact)
}