package com.bykirilov.kontur_android_test_task.view

import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            viewModel.preferContacts()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        binding = FragmentContactsListBinding.inflate(layoutInflater)
        val view = binding.root

        val adapter = ContactListAdapter.getAdapter(object : OnItemClickListener {
            override fun onItemClicked(contact: Contact) {
                activity!!.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container,
                            ContactDetailsFragment.newInstance(Bundle().apply {
                                putString(ContactDetailsFragment.CONTACT_ID, contact.id) }))
                        .addToBackStack("details")
                        .commit()
            }
        })

        binding.contactRecyclerview.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.updateContacts()
        }

        binding.search.addTextChangedListener {
            adapter.filter.filter(it)
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contacts?.let {
                adapter.submitList(it, true)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading: Boolean ->
            binding.loading.visibility = when (isLoading) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            binding.swipeRefreshLayout.visibility = when (isLoading) {
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