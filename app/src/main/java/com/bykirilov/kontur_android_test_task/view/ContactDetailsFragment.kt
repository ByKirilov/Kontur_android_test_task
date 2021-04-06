package com.bykirilov.kontur_android_test_task.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bykirilov.kontur_android_test_task.ContactsApplication
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.databinding.FragmentContactDetailsBinding
import com.bykirilov.kontur_android_test_task.viewModel.ContactDetailsViewModel
import com.bykirilov.kontur_android_test_task.viewModel.ContactDetailsViewModelFactory

class ContactDetailsFragment : Fragment() {

    private val viewModel: ContactDetailsViewModel by viewModels {
        ContactDetailsViewModelFactory((activity?.application as ContactsApplication).repository)
    }

    private lateinit var binding: FragmentContactDetailsBinding

    private var contactId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contactId = it.getString(CONTACT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactDetailsBinding.inflate(layoutInflater)
        val view = binding.root

        contactId?.let { id ->
            viewModel.loadContactById(id)
            viewModel.contact.observe(viewLifecycleOwner) {
                it?.let { contact ->
                    binding.contactName.text = contact.name
                    binding.contactPhone.text = contact.phone
                    binding.contactPhone.setOnClickListener {
                        viewModel.dialPhoneNumber(requireContext(), contact.phone)
                    }
                    binding.contactTemperament.text = contact.temperament
                    binding.contactEducationPeriod.text = "${contact.educationStart} - ${contact.educationEnd}"
                    binding.contactBiography.text = contact.biography
                }
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(contactData: Bundle) =
            ContactDetailsFragment().apply {
                arguments = contactData
            }

        const val CONTACT_ID = "contact_id"
    }
}