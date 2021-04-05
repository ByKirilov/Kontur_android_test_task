package com.bykirilov.kontur_android_test_task.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bykirilov.kontur_android_test_task.ContactsApplication
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.databinding.FragmentContactsListBinding
import com.bykirilov.kontur_android_test_task.model.database.Contact
import com.bykirilov.kontur_android_test_task.viewModel.ContactsListViewModel
import com.bykirilov.kontur_android_test_task.viewModel.ContactsListViewModelFactory
import com.google.android.material.snackbar.Snackbar

class ContactsListFragment : Fragment() {

    private lateinit var binding: FragmentContactsListBinding

    private val viewModel: ContactsListViewModel by viewModels {
        ContactsListViewModelFactory((activity?.application as ContactsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contacts_list, container, false)

        viewModel.preferContacts()

        binding = FragmentContactsListBinding.inflate(layoutInflater)

        val adapter = ContactListAdapter()
        val recyclerView = view.findViewById<RecyclerView>(R.id.contact_recyclerview)
        recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contacts?.let {
                adapter.submitList(it)
            }
        }

        val loadingView = view.findViewById<ProgressBar>(R.id.loading)
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            loadingView.visibility = when (isLoading) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            recyclerView.visibility = when (isLoading) {
                true -> View.GONE
                false -> View.VISIBLE
            }
        }

        viewModel.showSnackBar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ContactsListFragment()
    }
}