package com.bykirilov.kontur_android_test_task.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.databinding.FragmentContactsListBinding
import com.bykirilov.kontur_android_test_task.viewModel.ContactsListViewModel

class ContactsListFragment : Fragment() {

    lateinit var binding: FragmentContactsListBinding

    private lateinit var viewModel: ContactsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsListBinding.inflate(layoutInflater)

        viewModel = ViewModelProviders.of(this).get(ContactsListViewModel::class.java)
        viewModel.getContacts()

        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsListFragment()
    }
}