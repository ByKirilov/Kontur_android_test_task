package com.bykirilov.kontur_android_test_task.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.databinding.FragmentContactsListBinding

class ContactsListFragment : Fragment() {

    lateinit var binding: FragmentContactsListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsListBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_contacts_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsListFragment()
    }
}