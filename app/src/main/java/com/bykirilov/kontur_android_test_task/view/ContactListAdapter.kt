package com.bykirilov.kontur_android_test_task.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bykirilov.kontur_android_test_task.R
import com.bykirilov.kontur_android_test_task.model.database.Contact

@Suppress("UNCHECKED_CAST")
class ContactListAdapter(private val itemClickListener: OnItemClickListener)
    : ListAdapter<Contact, ContactListAdapter.ContactViewHolder>(ContactComparator()),
    Filterable {

    private lateinit var originalContacts: List<Contact>

    fun submitList(list: List<Contact>?, updateList: Boolean = false) {
        submitList(list)
        if (updateList) {
            originalContacts = this.currentList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, itemClickListener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()

                if (constraint.isNullOrEmpty()) {
                    results.values = originalContacts
                    results.count = originalContacts.size
                }
                else {
                    val filteredContacts = mutableListOf<Contact>()

                    for (contact in originalContacts) {
                        if (contact.name.contains(constraint) ||
                                contact.phone.contains(constraint)) {
                            filteredContacts.add(contact)
                        }
                    }
                    results.values = filteredContacts
                    results.count = filteredContacts.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                submitList(results?.values as List<Contact>?, false)
            }
        }
    }

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val contactNameItemView: TextView = itemView.findViewById(R.id.contact_name)
        private val contactPhoneItemView: TextView = itemView.findViewById(R.id.contact_phone)
        private val contactHeightItemView: TextView = itemView.findViewById(R.id.contact_height)

        fun bind(contact: Contact?,
                 itemClickListener: OnItemClickListener) {
            contactNameItemView.text = contact?.name
            contactPhoneItemView.text = contact?.phone
            contactHeightItemView.text = contact?.height.toString()

            contact?.let { contact ->
                itemView.setOnClickListener{ itemClickListener.onItemClicked(contact) }
            }
        }

        companion object {
            fun create(parent: ViewGroup): ContactViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return ContactViewHolder(view)
            }
        }
    }

    class ContactComparator : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.phone == newItem.phone
                    && oldItem.height == newItem.height
        }
    }

    companion object ContactListAdapterFactory {
        private var adapter: ContactListAdapter? = null
        fun getAdapter(itemClickListener: OnItemClickListener) : ContactListAdapter {
            if (adapter == null) {
                adapter = ContactListAdapter(itemClickListener)
            }
            return adapter as ContactListAdapter
        }
    }
}